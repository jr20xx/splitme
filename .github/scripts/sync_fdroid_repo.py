from __future__ import annotations

import os
import shutil
import subprocess
import sys
import re
import base64
from pathlib import Path

def run(command: list[str], *, cwd: Path | None = None, check: bool = True, display: str | None = None, capture_output = False) -> subprocess.CompletedProcess:
    """Run a command, log it (optionally redacted) and return its result."""
    print(f'{display or " ".join(str(c) for c in command)}')
    return subprocess.run(command, cwd=cwd, check=check, text=True, capture_output = capture_output)

def die(msg: str, code: int = 1) -> None:
    print(f"ERROR: {msg}", file=sys.stderr)
    sys.exit(code)

def main() -> int:
    github_repo_location = "jr20xx/fdroid"
    fdroid_repo_url = f"https://github.com/{github_repo_location}"
    fdroid_repo_target_branch = "main"
    fdroid_repo_clone_directory_path = Path("fdroid").resolve()

    release_apk_file_path = Path("app/build/outputs/apk/release/app-release.apk").resolve()
    release_apk_destiny_path = fdroid_repo_clone_directory_path / "fdroid" / "repo"

    # Clone the custom FDroid repository
    if fdroid_repo_clone_directory_path.exists():
        print(f"Removing existing {fdroid_repo_clone_directory_path}")
        shutil.rmtree(fdroid_repo_clone_directory_path)
    try:
        run(
            ["git", "clone", "--depth", "1", "--branch", fdroid_repo_target_branch,
            fdroid_repo_url, str(fdroid_repo_clone_directory_path)],
            display="Cloning custom FDroid repo",
        )
    except subprocess.CalledProcessError:
        die("Failed to clone the custom FDroid repo")

    # Verify the cloned version in search of a .git directory
    if not (fdroid_repo_clone_directory_path / ".git").is_dir():
        die(f"clone of {fdroid_repo_url} doesn't have a .git directory")
    print("The cloned version of the FDroid repo has a .git directory")

    # Verify if there's a generated release APK
    if not release_apk_file_path.is_file():
        die(f"Release APK not found at {release_apk_file_path}")

    # Obtain the package name and version of the released APK using aapt2
    basic_release_apk_metadata_output = run(["aapt2", "dump", "badging", release_apk_file_path], capture_output = True).stdout
    release_apk_package_name_matches = re.search(r"^package: name='([^']*)'", basic_release_apk_metadata_output, re.MULTILINE)
    release_apk_version_matches = re.search(r"versionName='([^']*)'", basic_release_apk_metadata_output)

    if not release_apk_package_name_matches:
        die("Couldn't determine the package name of the released APK file")
    if not release_apk_version_matches:
        die("Couldn't determine the version of the released APK file")

    release_apk_package_name = release_apk_package_name_matches.group(1)
    release_apk_version = release_apk_version_matches.group(1)
    print(f"Successfully obtained the package name and version of the generated release APK")

    # Grab the metadata files and, if different or missing at the custom FDroid repo, update it or copy it
    fdroid_metadata_origin_file_path = Path(f".fdroid/{release_apk_package_name}.yml").resolve()
    if not fdroid_metadata_origin_file_path.is_file():
        die("Application metadata is not present under the .fdroid directory")

    fdroid_metadata_destiny_file_path = fdroid_repo_clone_directory_path / "fdroid" / "metadata" / f"{release_apk_package_name}.yml"
    original_metadata_file_bytes = fdroid_metadata_origin_file_path.read_bytes()
    if fdroid_metadata_destiny_file_path.is_file():
        if original_metadata_file_bytes != fdroid_metadata_destiny_file_path.read_bytes():
            print(f"Updating metadata for {release_apk_package_name}")
            fdroid_metadata_destiny_file_path.write_bytes(original_metadata_file_bytes)
    else:
        print(f"Copying metadata for {release_apk_package_name}")
        fdroid_metadata_destiny_file_path.parent.mkdir(parents=True, exist_ok=True)
        fdroid_metadata_destiny_file_path.write_bytes(original_metadata_file_bytes)

    # Copy the release APK file to the custom FDroid repo
    release_apk_destiny_path.mkdir(parents=True, exist_ok=True)
    shutil.copy2(release_apk_file_path, release_apk_destiny_path / f"{release_apk_package_name}_v{release_apk_version}.apk")
    print(f"Copied the release APK file to the custom FDroid repo files tree")

    # Fetching the access token and encoding it as Base64
    token = os.environ.get("GH_ACCESS_TOKEN") or die("Access token is required")
    auth_header = base64.b64encode(f"x-access-token:{token}".encode()).decode()

    # Commit and push any changes    
    run(["git", "config", "user.name",  "github-actions[bot]"], cwd=fdroid_repo_clone_directory_path)
    run(["git", "config", "user.email", "github-actions[bot]@users.noreply.github.com"], cwd=fdroid_repo_clone_directory_path)
    run(["git", "add", "-A"], cwd=fdroid_repo_clone_directory_path)

    # `git diff --cached --quiet` exits 0 when there are no staged changes
    if subprocess.run(["git", "diff", "--cached", "--quiet"],
                      cwd=fdroid_repo_clone_directory_path).returncode == 0:
        print("Nothing to commit")
        return 0

    commit_msg = (f"Automated update of the {release_apk_package_name} app")
    run(["git", "commit", "-m", commit_msg], cwd=fdroid_repo_clone_directory_path)

    try:
        run(["git", "-c", f"http.https://github.com/.extraheader=AUTHORIZATION: basic {auth_header}",
             "push", "origin", fdroid_repo_target_branch],
             cwd=fdroid_repo_clone_directory_path,
             display=f"Pushing changes to {github_repo_location}",)
    except subprocess.CalledProcessError:
        die(f"Failed to push the changes to {fdroid_repo_url}")

    print("Synchronization process completed successfully")
    return 0

if __name__ == "__main__":
    sys.exit(main())
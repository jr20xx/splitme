<div align="center">
	<img src="https://raw.githubusercontent.com/jr20xx/splitme/master/.github/resources/banner.jpg" width="100%" alt="Banner">
</div>

## Status

[![Build project](https://img.shields.io/github/actions/workflow/status/jr20xx/splitme/build_project.yml?branch=master&label=Project%20Build)](https://github.com/jr20xx/splitme/actions/workflows/build_project.yml)
[![Repository Size](https://img.shields.io/github/repo-size/jr20xx/splitme?label=Repository+Size)](https://github.com/jr20xx/splitme)
[![License](https://img.shields.io/github/license/jr20xx/splitme?label=License)](https://raw.githubusercontent.com/jr20xx/splitme/master/LICENSE)

## Table of Contents

- [Description of the project](#description-of-the-project)
- [Changelog](#changelog)
- [Contribution](#contribution)
- [Building process](#building-process)
- [Credits](#credits)
- [Preview](#preview)
- [Disclaimer](#disclaimer)

## Description of the project

This repo contains a fork of [SplitMe](https://github.com/hidroh/splitme/), a project created by [Ha Duy Trung](https://github.com/hidroh) to add a quick settings tile to toggle the split screen functionality in devices with Android 9 (Pie) and above, where this functionality may be hidden.

The fork was created to try to bring a little more life into the original project, by adding some additional features and experimenting a bit.

## Changelog

- Update of the project structure.
- Compile SDK updated to 33.
- Enabled Data and View bindings.
- Addition of AppCompat libraries.
- Checks to prevent battery optimization from killing the app.
- Declaration of the app as a Device Administrator to prevent accidental uninstalls.
- Addition of an activity to centralize the requests of all the permissions.
- Update of the default strings.
- Addition of Spanish translations.
- Addition of Punjabi translations.
- Addition of an icon for the app.
- Removal of the deprecated dependency 'LocalBroadcastManager'.
- Code cleanup.
- Addition of a workflow to build a debug APK.
- Addition of a workflow to build a release APK.
- Addition of workflows to build the project when changes are pushed or when a pull request is opened.
- Addition of the GPL-3.0 license.

## Contribution

We value a lot any kind of contribution and we encourage you to submit pull requests and to provide tutorials or other relevant content that might help to improve this project. 

You can also contribute to this repo by adding a star to it and/or sharing the link if you find it helpful in some way. Any form of help will be highly appreciated.

## Building process

This project was coded using [Android Studio Giraffe (2022.3.1 Patch 3)](https://redirector.gvt1.com/edgedl/android/studio/ide-zips/2022.3.1.21/android-studio-2022.3.1.21-linux.tar.gz) in Arch Linux.

In order to follow some [Continuous Integration](https://en.wikipedia.org/wiki/Continuous_integration) principles, there's [a workflow](https://github.com/jr20xx/splitme/actions/workflows/build_project.yml) that automatically builds the project when changes are pushed or when a pull request is opened. Besides that workflow, there are other two workflows that were created to build two different versions of the APK file resultant from this project: a [debug version](https://github.com/jr20xx/splitme/actions/workflows/build_debug_apk.yml) and a [release version](https://github.com/jr20xx/splitme/actions/workflows/build_release_apk.yml). 

When you execute those last two mentioned workflows, the resultant debug version will be signed with a debug key, while the release version will be signed with a custom key created by myself. That means that the resultant APK files won't have the same signature, so be careful when installing the debug or the release version as issues can appear because Android security policies may block the installation of new versions of apps if their signatures don't match.

If you don't want to use any of the workflows previously described to build the project and/or want to build everything on your own, first create a local copy of the project on your device by running the following command in a terminal:
```bash
git clone https://github.com/jr20xx/splitme
```
After that, just open the project with Android Studio, wait until it sets all things up and you'll be good to go.

## Credits

### Special thanks to:
- [Ha Duy Trung](https://github.com/hidroh), for the source code of the original project.
- [K S Maan](https://github.com/KSMaan45), for most of the ideas for the changes, for testing the app and for the Punjabi translations.

## Preview:
<img src="https://raw.githubusercontent.com/jr20xx/splitme/master/.github/resources/main.webp" alt="Main Activity">

<img src="https://raw.githubusercontent.com/jr20xx/splitme/master/.github/resources/demo.gif" alt="Demo">

## Disclaimer
> [!WARNING]
>- The use of this app in heavily modified custom ROMs is discouraged because unexpected behaviors may arise.
>- Any of the people involved in the development of this app can't be considered responsible for any potential damage or issue that may occur to you or your device while using the app.
>- Use the app at your own risk and consideration.
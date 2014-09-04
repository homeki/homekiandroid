# Homeki Android

[![Circle CI](https://circleci.com/gh/homeki/homekiandroid/tree/develop.png?style=badge)](https://circleci.com/gh/homeki/homekiandroid/tree/develop)

This is the Android app/client for Homeki server.

## Local development

Prerequisites:
 * Android SDK
  - Android 4.4W (API 20)
  - Android Support Repository
  - Google Repository

The project uses gradle for building. In IntelliJ (or Android Studio), simply load the project from gradle sources.

## Release

 1. Increment `versionCode` and `versionName` in `app/build.gradle`.
 2. Push to master.
 3. Go to https://circleci.com/gh/homeki/homekiandroid/tree/master, click the newly finished build.
 4. Upload the **Build artifact** `app-release.apk` to [Google Play Developer Console](https://play.google.com/apps/publish/).

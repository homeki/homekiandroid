# Homeki Android

This is the Android app/client for Homeki server.

## Local development

Prerequisites:
 * Android SDK
  - Android 4.4W (API 20)
  - Android Support Repository
  - Android Support Library
  - Google Play services
  - Google Repository

The project uses gradle for building. In IntelliJ (or Android Studio), simply load the project from gradle sources.

## Build release

 1. Re-enable the commented lines in `app/build.gradle` (regarding `signingConfigs`).
 2. Set the correct values in `myConfig`.
 3. Increment `versionCode` and `versionName`.
 4. Run `./gradlew assembleRelease`.
 5. Upload the release APK found in `app/build/apk/app-release.apk` to Google Play Developer Console.
 6. *DO NOT* commit the passwords (restore everything except version increments in `app/build.gradle`).
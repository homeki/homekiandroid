#!/bin/bash

# This scripts extracts the keystore from environment variable, builds
# the release APK and publishes it as a CircleCI artifact.

set -e
echo $KEYSTORE | base64 --decode > homeki.keystore
set +e
./gradlew assembleRelease
rm homeki.keystore
set -e
cp app/build/outputs/apk/app-release.apk $CIRCLE_ARTIFACTS

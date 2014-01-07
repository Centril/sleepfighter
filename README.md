# SleepFighter

An Android alarm app developed for the Software Engineering course DAT255 at Chalmers University of Technology (CTH).

## Project Vision

For sleepy people who want a fun way to quickly wake up, SleepFighter is an alarm app that compels the user to wake up faster. Unlike other alarm apps, our product is hard to turn off in a drowsy state, as it requires the focus of the user in order to solve one of many short, engaging and creative challenges.

## How to install
**Required Android Version:** 2.3 (API level 9)

Find an APK file for the latest version in the "build" folder and install it onto your device.

## Set up development environment (Android Studio)

**Android API Target (required):** 19 (4.4)

Open up command line / terminal and cd to /application/
Note: / is the root of the local git clone.
Run the following:

    ./gradlew cleanIdea idea

Intellij files should have been generated, open up the project in Android Studio.

## Set up development environment (Gradle)

**Android API Target (required):** 19 (4.4)

Open up command line / terminal and cd to /application/
Note: / is the root of the local git clone.
Run the following to build the project:

    ./gradlew assemble

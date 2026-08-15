# Wedding Planner 4 U (WP4U)

WP4U is an Android app that helps wedding planners keep their inspiration in one place. Instead of scattering ideas across screenshots, saved links, and photo galleries, planners can sort images into clear wedding categories.

## About the Project

The app comes with pre-made categories like Wedding Dresses, Venues, and Food, and users upload their own images into each one. Images can be replaced or deleted anytime so boards stay current. Every user has their own account, and all account details and images are stored locally in a Room (SQLite) database on the device. Boards are per-user, so each account only sees its own images.

This was built for CST8319 (Software Development Project) using an Agile Scrum workflow.

## Features

Users can sign up and sign in, and the signed-in user is tracked across the whole app through a shared auth layer. There are five pre-made categories: Wedding Dresses, Venues, Food, Flowers, and Invitations. Users upload images to any category, long-press an image to delete or replace it, and drag the handle on a tile to reorder images. Boards are per-user, so each account only sees its own images, and every new account starts off pre-loaded with a set of sample images on all five boards. Everything is saved locally and persists across restarts. The app also includes unit and instrumented tests covering login, categories, boards, and the database.

## Tech Stack

Android Studio is the IDE. The app is written in Kotlin with XML layouts. Room (SQLite) handles local storage for accounts, categories, and images, and Glide handles image loading and display. Testing uses JUnit and Espresso. GitHub is used for version control and collaboration, and the team coordinates on Discord.

## Architecture

The app uses a repository pattern. UI components talk to a BoardRepository, which is backed by a Room implementation (RoomBoardRepository), plus a shared AuthRepository that keeps the signed-in user visible everywhere. A ServiceLocator provides these so implementations can be swapped easily.

There's also an ImageStorage helper for saving image files, and a SampleImageSeeder that copies the sample set from assets/seed/ onto each new account. The five categories are seeded with fixed IDs (dresses=1, venues=2, food=3, flowers=4, invitations=5) when the database is first created, which matches the seed folders.

## Target Platform

The app runs on Android and is written in Kotlin. It supports Android 9.0 (API 28) and up, targets and compiles against Android 16 (API 36), and uses Java 11 compatibility.

## Environment Setup

Here's how to get the project running.

### 1. Install what you need

Install Android Studio (latest stable) from https://developer.android.com/studio. It comes with the IDE, the Android SDK, Gradle, and a bundled JDK, so you don't need a separate Java install. You'll also need Git from https://git-scm.com to clone the repo.

### 2. Set up the Android SDK

In Android Studio, open Settings, then Languages & Frameworks, then Android SDK, and make sure you have the SDK Platform for API 36 (target) and API 28 (minimum), the SDK Build-Tools, the Android Emulator, and the SDK Platform-Tools.

### 3. Clone the repo

```bash
git clone https://github.com/aleblanc123/WP4U.git
```

### 4. Open and sync

Open the WP4U folder in Android Studio (File, then Open). It runs a Gradle sync automatically, which pulls down all the dependencies. Wait for it to finish and accept any SDK license prompts.

### 5. Dependencies

Gradle handles all dependencies during sync, so there's nothing to install by hand. The main ones are AndroidX (Core KTX, AppCompat, Activity, ConstraintLayout), Material Components, RecyclerView, Lifecycle ViewModel KTX, Room 3.0.0 (with the Room compiler running through KSP), Glide 4.16.0, and JUnit plus Espresso for testing. Room's annotation processing runs through KSP, set up in the Gradle plugins block.

### 6. Configuration

There's nothing to configure. No API keys or external credentials are needed since everything runs on a local Room database. The database, the five categories, and the sample images for a new account are all created automatically the first time that account launches. The app package is com.example.wp4u.

### 7. Run it

Create an Android Virtual Device in Device Manager, or plug in a real Android 9.0+ device with USB debugging on. Pick it from the target dropdown and hit Run to build, install, and launch.

If you're upgrading an emulator that already had an older build, uninstall the app once before running the new code so the categories seed correctly. This is a one-time thing and doesn't affect fresh installs.

## Running Tests

Run the tests from Android Studio, or from the command line:

```bash
./gradlew test
./gradlew connectedAndroidTest
```

Heads up: running the instrumented tests uninstalls the app afterward, which clears any accounts you made locally. That's expected and doesn't affect the results.

## Branching

Everyone works on their own branch and opens a pull request for review before merging into main. Branches include database-functionality for the Room work and feature branches for categories and image upload.

## Team

Alex LeBlanc handled the sign-in and sign-up pages, authentication, UI styling, tests, and the GitHub repo setup. Maliah Stiles built the Room DAOs, the database tests, and worked on documentation. R. Shema Yvan built the categories, image upload, delete, replace, and reorder, the per-user boards, and the sample image seeding. Reggie B. wrote the README and project documentation. Mohamed (Wangdong) provided the sample images and the class documentation.

## Project Status

The core is done. Accounts, per-user boards, image management, and sample-image seeding all work and persist across restarts. Future plans include letting users create their own categories and other added features.

## License

Built for educational purposes as part of CST8319 at Algonquin College.

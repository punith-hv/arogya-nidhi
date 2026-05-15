# ManeKelsa Android Application

## Overview

ManeKelsa is an Android application built using **Kotlin** and **Jetpack Compose**. The project uses **Firebase** services for authentication and backend support.

The app is designed with a minimal UI, modern Android architecture, and user-friendly navigation.

---

# Tech Stack

* Kotlin
* Jetpack Compose
* Android Studio
* Firebase Authentication
* Firebase Realtime Database / Firestore
* Gradle Kotlin DSL
* Material 3

---

# Requirements

Before running the project, make sure the following tools are installed:

## Software Required

| Tool           | Version            |
| -------------- | ------------------ |
| Android Studio | Hedgehog or Latest |
| JDK            | 17                 |
| Gradle         | 8.x                |
| Android SDK    | API 34 or above    |
| Kotlin         | Latest Stable      |

---

# Project Setup Instructions

## 1. Clone the Repository

```bash
git clone https://github.com/your-username/manekelsa.git
```

OR download the ZIP file and extract it.

---

## 2. Open Project in Android Studio

1. Open Android Studio
2. Click **Open**
3. Select the extracted project folder
4. Wait for Gradle Sync to complete

---

# Firebase Setup

## 3. Create Firebase Project

1. Open Firebase Console:
   [https://console.firebase.google.com/](https://console.firebase.google.com/)

2. Click **Create Project**

3. Enter project name:

```text
ManeKelsa
```

4. Continue and finish setup.

---

## 4. Add Android App to Firebase

1. Inside Firebase project click:

```text
Add App → Android
```

2. Enter package name:

```text
com.manekelsa.app
```

3. Register app.

---

## 5. Download google-services.json

1. Download the `google-services.json` file.
2. Paste it inside:

```text
app/google-services.json
```

---

# Gradle Configuration

## 6. Project-level build.gradle

Make sure the following plugin exists:

```gradle
plugins {
    id("com.google.gms.google-services") version "4.4.2" apply false
}
```

---

## 7. App-level build.gradle

Add these plugins:

```gradle
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}
```

Add Firebase dependencies:

```gradle
implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
implementation("com.google.firebase:firebase-auth")
implementation("com.google.firebase:firebase-database")
```

---

# Enable Jetpack Compose

Inside `app/build.gradle`:

```gradle
buildFeatures {
    compose = true
}

composeOptions {
    kotlinCompilerExtensionVersion = "1.5.14"
}
```

---

# Sync Project

## 8. Sync Gradle

Click:

```text
File → Sync Project with Gradle Files
```

Wait until sync completes successfully.

---

# Run the Application

## 9. Connect Device

### Option 1: Physical Device

1. Enable Developer Options
2. Enable USB Debugging
3. Connect mobile using USB cable

### Option 2: Emulator

1. Open Device Manager
2. Create Virtual Device
3. Start emulator

---

## 10. Run App

Click the ▶ Run button in Android Studio.

The application should build and install on the selected device.

---

# Release Build Setup

## 11. Generate Keystore

Run the following command inside project folder:

```bash
keytool -genkeypair -v -keystore manekelsa-app-keys.jks -keyalg RSA -keysize 2048 -validity 10000 -alias key0
```

Store the generated file safely.

---

## 12. Configure Signing

Inside `app/build.gradle`:

```gradle
android {
    signingConfigs {
        create("release") {
            storeFile = file("../manekelsa-app-keys.jks")
            storePassword = "YOUR_PASSWORD"
            keyAlias = "key0"
            keyPassword = "YOUR_PASSWORD"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
```

---

# Generate APK

## 13. Build APK

### Debug APK

```text
Build → Build APK(s)
```

### Release APK

```text
Build → Generate Signed Bundle / APK
```

---

# Common Errors & Fixes

## Gradle Verification Failed

Delete Gradle cache:

```text
C:\Users\YOUR_NAME\.gradle
```

Then re-sync project.

---

## Missing Keystore Error

Make sure:

* `manekelsa-app-keys.jks` exists
* Correct path is used
* Correct alias and password are provided

---

## App Crashing on Launch

Check:

* Firebase configured properly
* `google-services.json` added
* Internet permission added
* Correct package name used

Add internet permission in `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

---

## Firebase Authentication Error

Enable Email/Password sign-in:

```text
Firebase Console → Authentication → Sign-in Method
```

---

# Recommended Folder Structure

```text
app/
 ├── src/main/java/com/manekelsa/app
 ├── ui/
 ├── screens/
 ├── components/
 ├── navigation/
 ├── data/
 ├── utils/
 └── google-services.json
```

---

# Useful Commands

## Clean Project

```bash
./gradlew clean
```

## Build Debug APK

```bash
./gradlew assembleDebug
```

## Build Release APK

```bash
./gradlew assembleRelease
```

---

# Features

* User Authentication
* Firebase Integration
* Modern Compose UI
* Responsive Design
* Minimal Theme
* Android Native Performance

---

# Future Improvements

* Push Notifications
* Dark Mode
* Profile Management
* Job Tracking
* Admin Dashboard
* Cloud Storage Support

---

# Author

Developed using Kotlin and Jetpack Compose.

---

# License

This project is for educational and development purposes.

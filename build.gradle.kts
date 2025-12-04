// Top-level build file where you can add configuration options common to all sub-projects/modules.
// Repositories are now defined in settings.gradle.kts, so the 'buildscript' and 'allprojects' blocks are removed.

plugins {
    // Defines the Android Application plugin and its version for the project.
    alias(libs.plugins.android.application) apply false

    // Defines the Kotlin Android plugin and its version for the project.
    alias(libs.plugins.kotlin.android) apply false

    // Defines the Google Services plugin (for Firebase) and its version for the project.
    id("com.google.gms.google-services") version "4.4.1" apply false
}

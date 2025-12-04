plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    // Compose compiler plugin for Kotlin 2.0 projects
    id("org.jetbrains.kotlin.plugin.compose")

    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.campusconnectandcollab"
    // FIX: Updated compileSdk to 36 to meet the new dependency requirements.
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.campusconnectandcollab"
        // FIX: Updated minSdk to 23 to resolve the Manifest Merger Failed error.
        minSdk = 23
        // FIX: Updated targetSdk to 36 to match the compileSdk.
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        // NOTE: The compose compiler version is tied to the Kotlin plugin version.
        // The new `org.jetbrains.kotlin.plugin.compose` plugin often doesn't require this.
        // If you get errors later, you may need to update or remove this line.
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")

    // Compose & AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)

    debugImplementation(libs.androidx.compose.ui.tooling)
}

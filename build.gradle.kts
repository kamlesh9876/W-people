plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services) // Ensure Firebase services are included
}

android {
    namespace = "com.example.w_people"
    compileSdk = 35

    buildFeatures {
        viewBinding = true // Enables view binding
    }

    defaultConfig {
        applicationId = "com.example.w_people"
        minSdk = 32
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // If you use Kotlin in your project, add the Kotlin plugin like so:
    // apply plugin: 'kotlin-android'
}

dependencies {
    // Firebase BoM (Bill of Materials to manage all Firebase versions)
    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))

    // Firebase SDKs for authentication, firestore, storage, and analytics
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-database") // Firebase Realtime Database

    // Google Sign-In (authentication with Google)
    implementation("com.google.android.gms:play-services-auth:21.0.0")

    // Glide for image loading
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    // AndroidX libraries for UI components
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.8.0")

    // Lifecycle components for managing UI-related data
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata:2.7.0")

    // CircleImageView for profile pictures (if needed)
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // Testing dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

// Apply Firebase plugin at the bottom (it should be applied after dependencies)
apply(plugin = "com.google.gms.google-services")

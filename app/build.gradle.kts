plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    // Kotlin Annotation Processing Tool (KAPT) - Dagger Hilt gibi anotasyon tabanlı işlemler için gerekli
    id("kotlin-kapt")

    // Dagger Hilt kullanarak bağımlılık enjeksiyonu yapmamızı sağlayan eklenti
    id("dagger.hilt.android.plugin")

    id("com.google.gms.google-services")
}

android {
    namespace = "com.cloffygames.foodfleet"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cloffygames.foodfleet"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.storage.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation ("androidx.compose.material:material-icons-extended:1.5.0")

    implementation("androidx.navigation:navigation-compose:2.7.6")
    implementation ("com.google.code.gson:gson:2.10")

    implementation("androidx.lifecycle:lifecycle-viewmodel:2.5.1")
    implementation("androidx.activity:activity-ktx:1.6.1")
    implementation("androidx.compose.runtime:runtime-livedata:1.5.4")

    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")

    implementation("com.squareup.retrofit2:retrofit:2.6.0")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.5.0")

    // Lottie animasyon kütüphanesi
    implementation ("com.airbnb.android:lottie-compose:6.0.0")

    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.google.firebase:firebase-firestore:25.1.0")

    // Google Sign-In için play-services-auth bağımlılığı
    implementation("com.google.android.gms:play-services-auth:21.2.0")  // Sürümü ekledik

    // Glide resim kütüphanesi
    implementation ("com.github.skydoves:landscapist-glide:1.4.4")


}


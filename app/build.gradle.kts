plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    id("kotlin-kapt")

    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
}

android {
    namespace = "ru.netology.faceyoga"
    compileSdk = 36

    defaultConfig {
        applicationId = "ru.netology.faceyoga"
        minSdk = 24
        targetSdk = 36
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

    // ViewBinding
    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // Android base
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Coli
    implementation(libs.coil)

    // Lifecycle / MVVM
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Navigation (Single Activity)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Retrofit + OkHttp
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging)

    // RecyclerView
    implementation(libs.androidx.recyclerview)

    // Room (Offline)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

    // Hilt (DI)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Media (Video)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.ui)

    // Images
    implementation(libs.coil)

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.storage.ktx)
    implementation(libs.kotlinx.coroutines.play.services)

    // Lottie
    implementation(libs.lottie)


}

kapt {
    correctErrorTypes = true
}

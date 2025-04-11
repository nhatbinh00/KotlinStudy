plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.kotlinknowledge"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.kotlinknowledge"
        minSdk = 24
        targetSdk = 34
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
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
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.protolite.well.known.types)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.espresso.core)
    implementation(libs.play.services.basement)
    implementation(libs.cronet.api)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // libs
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.material3)
    implementation(libs.ui)
    // Android Studio Preview support
    implementation(libs.ui.tooling.preview)
    debugImplementation(libs.ui.tooling)
    // Optional - Integration with ViewModels
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // Optional - Integration with LiveData
    implementation(libs.runtime.livedata)
    // Optional - Integration with RxJava
    implementation(libs.androidx.runtime.rxjava2)
    // Optional - Integration with activities
    implementation(libs.androidx.activity.compose.v192)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler.v2511)
    // navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    // carousel pager
    implementation(libs.accompanist.pager)
    //loading image
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.material.icons.extended)
    // Result
    implementation(libs.kotlin.result)
    implementation(libs.kotlin.result.coroutine)

    implementation(libs.moshi)
    //room DB
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(files("libs/internalsdk-debug.aar"))
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}
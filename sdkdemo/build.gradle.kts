plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "io.tenmax.sdkdemo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tenmax.sdkdemo"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".public"
            manifestPlaceholders["publisherId"] = "0fe5b5e7c1"
            resValue("string", "interstitialId", "3644ec9ebb0d4ed4")
            resValue("string", "inlineId", "fcc96b4975ff4357")
            resValue("string", "topBannerId", "590bab0f1b6a482f")
            resValue("string", "bottomBannerId", "843e0771ab4c4d9f")
            resValue("string", "floatingId", "765c489949994641")
        }
        release {
            applicationIdSuffix = ".public"
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            manifestPlaceholders["publisherId"] = "0fe5b5e7c1"
            resValue("string", "interstitialId", "3644ec9ebb0d4ed4")
            resValue("string", "inlineId", "fcc96b4975ff4357")
            resValue("string", "topBannerId", "590bab0f1b6a482f")
            resValue("string", "bottomBannerId", "843e0771ab4c4d9f")
            resValue("string", "floatingId", "765c489949994641")
        }
        create("cht") {
            initWith(getByName("debug"))
            applicationIdSuffix = ".cht"
            manifestPlaceholders["publisherId"] = "0fe5b5e7c1"
            resValue("string", "interstitialId", "3644ec9ebb0d4ed4")
            resValue("string", "inlineId", "fcc96b4975ff4357")
            resValue("string", "topBannerId", "590bab0f1b6a482f")
            resValue("string", "bottomBannerId", "843e0771ab4c4d9f")
            resValue("string", "floatingId", "765c489949994641")
        }
        create("internal") {
            initWith(getByName("debug"))
            applicationIdSuffix = ".internal"
            manifestPlaceholders["publisherId"] = "0fe5b5e7c1"
            resValue("string", "interstitialId", "3644ec9ebb0d4ed4")
            resValue("string", "inlineId", "fcc96b4975ff4357")
            resValue("string", "topBannerId", "590bab0f1b6a482f")
            resValue("string", "bottomBannerId", "843e0771ab4c4d9f")
            resValue("string", "floatingId", "765c489949994641")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(files("$projectDir/libs/adkit.aar"))
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.play.services.ads.identifier)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

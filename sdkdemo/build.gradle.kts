plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "io.tenmax.sdkdemo"
    compileSdk = 35

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
            resValue("string", "floatingId", "75da160bd7164d62")
            resValue("string", "app_name", "TenMaxMobileSDKDemo")
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
            resValue("string", "floatingId", "75da160bd7164d62")
            resValue("string", "app_name", "TenMaxMobileSDKDemo")
        }
        // the following build types are internal use only, please do not use them
        create("cht") {
            initWith(getByName("debug"))
            applicationIdSuffix = ".cht"
            manifestPlaceholders["publisherId"] = "7ac1ab497e"
            resValue("string", "interstitialId", "aa098c5fdb3c42e8")
            resValue("string", "inlineId", "53d7675c53c64de6")
            resValue("string", "topBannerId", "aa7947ea8e104da3")
            resValue("string", "bottomBannerId", "2b07703707354bd6")
            resValue("string", "floatingId", "5417d8bf58b34bac")
            resValue("string", "videoInlineId", "cdc8203a5167467d")
            resValue("string", "app_name", "TenMaxMobileSDKDemo (CHT)")
        }
        create("internal") {
            initWith(getByName("debug"))
            applicationIdSuffix = ".internal"
            manifestPlaceholders["publisherId"] = "3a915e28ca"
            resValue("string", "interstitialId", "cc43cdfb576f4ef1")
            resValue("string", "inlineId", "537a62f3a29c4a18")
            resValue("string", "topBannerId", "79eb94fa2a0c40db")
            resValue("string", "bottomBannerId", "fd1cf37e2c064da8")
            resValue("string", "floatingId", "77938353b6a04d78")
            resValue("string", "app_name", "TenMaxMobileSDKDemo (Internal)")
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
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

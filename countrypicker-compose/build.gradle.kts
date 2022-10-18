plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "com.hbb20.countrypicker.compose"
    compileSdk = 33

    defaultConfig {
        minSdk = 23
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs += listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
        )
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.2.0-alpha08"
    }
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementKotlin(org.jetbrains.kotlin.config.KotlinCompilerVersion.VERSION)
    implementation(Deps.appCompat)
    implementation(Deps.constraintLayout)
    implementation(Deps.emoji)
    implementation(Deps.googleMaterialDesign)
    implementation(Deps.timber)
    implementTesting()

    implementProject("countrypicker")
    implementProject("flagpack1")

    implementation("androidx.compose.ui:ui:1.2.1")
    // Tooling support (Previews, etc.)
    implementation("androidx.compose.ui:ui-tooling:1.2.1")
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation("androidx.compose.foundation:foundation:1.2.1")

//    implementation("androidx.compose.material3:material3:1.0.0-rc01")
}

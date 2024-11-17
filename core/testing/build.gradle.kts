plugins {
    alias(libs.plugins.todo.android.library)
    alias(libs.plugins.todo.android.library.compose)
    alias(libs.plugins.todo.android.hilt)
}

android {
    namespace = "com.example.testing"

    defaultConfig {
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
}

dependencies {
    api(libs.junit)
    api(libs.activity.compose)
    api(libs.androidx.test.ext.junit)
    api(libs.roborazzi)
    api(libs.roborazzi.compose)
    api(libs.roborazzi.junit.rule)
    api(libs.ui.test.junit4)
    api(libs.robospec)
    api(libs.robolectric)
    api(libs.hilt.android.testing)
    ksp(libs.hilt.compiler)
    api(projects.core.model)
    api(libs.turbine)
    api(libs.mockk)
    debugApi(libs.ui.test.manifest)
}

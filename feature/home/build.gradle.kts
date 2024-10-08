plugins {
    alias(libs.plugins.todo.android.library)
    alias(libs.plugins.todo.android.library.compose)
    alias(libs.plugins.todo.android.feature)
    alias(libs.plugins.todo.android.hilt)
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "com.example.home"

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

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)

    testImplementation(libs.robolectric)
    testImplementation(projects.core.testing)
    testImplementation(libs.mockk)
    androidTestImplementation(projects.core.testing)
}
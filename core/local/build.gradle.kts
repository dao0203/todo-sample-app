plugins {
    alias(libs.plugins.todo.android.library)
    alias(libs.plugins.todo.android.hilt)
    alias(libs.plugins.todo.android.room)
}

android {
    namespace = "com.example.local"

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
    api(projects.core.model)
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    testImplementation(libs.junit)
}

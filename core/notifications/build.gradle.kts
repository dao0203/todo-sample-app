plugins {
    alias(libs.plugins.todo.android.library)
    alias(libs.plugins.todo.android.hilt)
}

android {
    namespace = "com.example.notifications"
}

dependencies {
    api(projects.core.model)
    api(projects.core.common)

    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)
}

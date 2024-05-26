import com.android.build.gradle.LibraryExtension
import com.example.todo_sample_app.configureKotlinAndroid
import com.example.todo_sample_app.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 34

                dependencies {
                    add("implementation", libs.findLibrary("kotlinx-datetime").get())
                }
            }
        }
    }
}

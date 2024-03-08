import com.android.build.gradle.LibraryExtension
import com.example.todo_sample_app.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }
            extensions.configure<LibraryExtension> {
                dependencies {
                    add("api", project(":core:model"))
                    add("implementation", project(":core:repository"))
                    add("implementation", project(":core:designsystem"))

                    add("implementation", libs.findLibrary("lifecycle.viewmodel.compose").get())
                    add("implementation", libs.findLibrary("lifecycle.runtime.compose").get())

                    add("implementation", libs.findLibrary("hilt.navigation.compose").get())
                }
            }
        }
    }
}

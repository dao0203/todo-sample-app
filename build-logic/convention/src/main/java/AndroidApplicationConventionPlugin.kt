import com.android.build.api.dsl.ApplicationExtension
import com.example.todo_sample_app.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 34
            }

            dependencies {
                add("implementation", project(":core:repository"))
                add("implementation", project(":core:model"))
                add("implementation", project(":core:designsystem"))

                add("implementation", project(":feature:home"))
            }
        }
    }
}

import com.android.build.api.dsl.ApplicationExtension
import com.example.todo_sample_app.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationComposeConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }
            extensions.configure<ApplicationExtension> {
                configureAndroidCompose(this)
                defaultConfig.targetSdk = 34
            }
        }
    }
}
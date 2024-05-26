enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "todo-sample-app"
include(":app")

include(":core:repository")
include(":core:local")
include(":core:model")
include(":core:testing")
include(":core:designsystem")
include(":core:notifications")
include(":feature:home")
include(":core:common")

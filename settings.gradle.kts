pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    includeBuild("plugins/test-report-plugin")
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "gradle-configurations-playground"
include(":app")
include(":feature-1-android")
include(":feature-2-kotlin")
include(":feature-3-android")
include(":feature-4-kotlin")
include(":feature-5-android")
include(":feature-6-kotlin")
include(":feature-7-android")
include(":feature-8-kotlin")
include(":feature-9-android")

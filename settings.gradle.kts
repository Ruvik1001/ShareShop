pluginManagement {
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

rootProject.name = "Share Shop"
include(":app")
include(":core")
include(":data")
include(":domain")
include(":features")
include(":features:sign-in")
include(":features:sign-up")
include(":features:reset_password")
include(":features:home")
include(":features:friends")
include(":features:profile")
include(":features:product_list")
include(":features:product_list_settings")

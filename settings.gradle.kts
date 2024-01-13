pluginManagement {
    repositories {
        maven (  "https://maven.aliyun.com/repository/jcenter" )
        maven (  "https://maven.aliyun.com/repository/google" )
        maven (  "https://maven.aliyun.com/repository/central" )
        maven (  "https://maven.aliyun.com/repository/gradle-plugin" )
        google()
        mavenCentral()
        gradlePluginPortal()

        flatDir {
            dirs("libs")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven (  "https://maven.aliyun.com/repository/jcenter" )
        maven (  "https://maven.aliyun.com/repository/google" )
        maven (  "https://maven.aliyun.com/repository/central" )
        maven (  "https://maven.aliyun.com/repository/gradle-plugin" )
        google()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")

        flatDir {
            dirs("libs")
        }
    }
}

// TODO: Update project"s name.
rootProject.name = "GodotTapTapSDK"
include(":plugin")

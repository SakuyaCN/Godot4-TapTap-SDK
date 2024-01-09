import com.android.build.gradle.internal.tasks.factory.dependsOn

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.kezong.fat-aar")
}

// TODO: Update value to your plugin's name.
val pluginName = "GodotTapTapSDK"

// TODO: Update value to match your plugin's package name.
val pluginPackageName = "com.sakuya.godot_taptap"

android {
    namespace = pluginPackageName
    compileSdk = 33

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        minSdk = 21

        manifestPlaceholders["godotPluginName"] = pluginName
        manifestPlaceholders["godotPluginPackageName"] = pluginPackageName
        buildConfigField("String", "GODOT_PLUGIN_NAME", "\"${pluginName}\"")
        setProperty("archivesBaseName", pluginName)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("org.godotengine:godot:4.2.1.stable")

    compileOnly(files("libs/TapBootstrap_3.27.0.aar"))
    compileOnly(files("libs/TapCommon_3.27.0.aar"))
    compileOnly(files("libs/TapLogin_3.27.0.aar"))
    compileOnly(files("libs/AntiAddiction_3.27.0.aar"))
    compileOnly(files("libs/AntiAddictionUI_3.27.0.aar"))
    compileOnly(files("libs/TapConnect_3.27.0.aar"))
    compileOnly(files("libs/TapMoment_3.27.0.aar"))

    compileOnly("cn.leancloud:storage-android:8.2.19")
    compileOnly("cn.leancloud:realtime-android:8.2.19")
}

// BUILD TASKS DEFINITION
val copyDebugAARToDemoAddons by tasks.registering(Copy::class) {
    description = "Copies the generated debug AAR binary to the plugin's addons directory"
    from("build/outputs/aar")
    include("$pluginName-debug.aar")
    into("demo/addons/$pluginName/bin/debug")
}

val copyReleaseAARToDemoAddons by tasks.registering(Copy::class) {
    description = "Copies the generated release AAR binary to the plugin's addons directory"
    from("build/outputs/aar")
    include("$pluginName-release.aar")
    into("demo/addons/$pluginName/bin/release")
}

val copyLibsAARToDemoAddons by tasks.registering(Copy::class) {
    description = "Copies the generated release AAR binary to the plugin's addons directory"
    from("libs")
    include("TapBootstrap_3.27.0.aar")
    include("TapCommon_3.27.0.aar")
    include("TapLogin_3.27.0.aar")
    include("AntiAddiction_3.27.0.aar")
    include("AntiAddictionUI_3.27.0.aar")
    include("TapConnect_3.27.0.aar")
    include("TapMoment_3.27.0.aar")
    into("demo/addons/$pluginName/bin")
}

val cleanDemoAddons by tasks.registering(Delete::class) {
    delete("demo/addons/$pluginName")
}

val copyAddonsToDemo by tasks.registering(Copy::class) {
    description = "Copies the export scripts templates to the plugin's addons directory"

    dependsOn(cleanDemoAddons)
    finalizedBy(copyDebugAARToDemoAddons)
    finalizedBy(copyReleaseAARToDemoAddons)
    finalizedBy(copyLibsAARToDemoAddons)

    from("export_scripts_template")
    into("demo/addons/$pluginName")
}

tasks.named("assemble").configure {
    finalizedBy(copyAddonsToDemo)
}

tasks.named<Delete>("clean").apply {
    dependsOn(cleanDemoAddons)
}

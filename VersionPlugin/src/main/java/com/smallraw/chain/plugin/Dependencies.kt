package com.smallraw.chain.plugin

object Versions {
    val kotlin_version = "1.6.10"
    val ksp_version = "1.6.10-1.0.4"

    val test_junit = "4.13.2"
    val test_android_junit = "1.1.3"
    val test_espresso = "3.4.0"
    val test_runner = "1.2.0"

    val androidx = "1.0.0"
    val appcompat = "1.2.0"
    val navigation = "2.3.0"
    val fragment = "1.3.2"
    val recyclerview = "1.1.0"
    val cardview = "1.0.0"
    val constraintLayout = "2.0.4"
    val material = "1.1.0-alpha08"
    val kotlin = "1.4.31"
    val kotlinxCoroutinesCore = "1.4.2"
    val okhttp = "3.10.0"
    val retrofit = "2.6.0"
    val room = "2.3.0-rc01"
}

object Names {
    val applicationId = "com.smallraw.foretime.app"
    val versionName = "1.0.0"
}

object Deps {
    val test_junit = "junit:junit:${Versions.test_junit}"
    val test_runner = "androidx.test:runner:${Versions.test_runner}"
    val test_android_junit= "androidx.test.ext:junit:${Versions.test_android_junit}"
    val test_espresso = "androidx.test.espresso:espresso-core:${Versions.test_espresso}"

    // jetpack see https://developer.android.google.cn/jetpack
    // see AndroidX version update https://developer.android.google.cn/jetpack/androidx/releases
    val constraintlayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    val material = "com.google.android.material:material:${Versions.material}"
    val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    val cardview = "androidx.cardview:cardview:${Versions.cardview}"
    val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"

    val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    val kotlin_gradle_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    val retrofit_rxjava = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"

    val navigation_runtime = "androidx.navigation:navigation-runtime:${Versions.navigation}"
    val navigation_runtime_ktx =
        "androidx.navigation:navigation-runtime-ktx:${Versions.navigation}"
    val navigation_fragment = "androidx.navigation:navigation-fragment:${Versions.navigation}"
    val navigation_fragment_ktx =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    val navigation_testing = "androidx.navigation:navigation-testing:${Versions.navigation}"
    val navigation_ui = "androidx.navigation:navigation-ui:${Versions.navigation}"
    val navigation_ui_ktx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    val navigation_safe_args_plugin =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
}
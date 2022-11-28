package com.smallraw.chain.plugin

object Versions {
    const val kotlin = "1.7.20"
    // https://developer.android.com/jetpack/androidx/releases/compose
    const val compose = "1.3.1"

    const val test_junit = "4.13.2"
    const val test_android_junit = "1.1.3"
    const val test_espresso = "3.4.0"
    const val test_runner = "1.2.0"

    const val androidx = "1.0.0"
    const val appcompat = "1.2.0"
    const val navigation = "2.3.0"
    const val fragment = "1.3.2"
    const val recyclerview = "1.1.0"
    const val cardview = "1.0.0"
    const val constraintLayout = "2.0.4"
    const val material = "1.1.0-alpha08"
    const val kotlinxCoroutinesCore = "1.4.2"
    const val okhttp = "3.10.0"
    const val retrofit = "2.6.0"
    const val room = "2.3.0-rc01"

}

object Names {
    const val applicationId = "com.smallraw.foretime.app"
    const val versionName = "1.0.0"
}

object Deps {
    const val test_junit = "junit:junit:${Versions.test_junit}"
    const val test_runner = "androidx.test:runner:${Versions.test_runner}"
    const val test_android_junit = "androidx.test.ext:junit:${Versions.test_android_junit}"
    const val test_espresso = "androidx.test.espresso:espresso-core:${Versions.test_espresso}"

    // jetpack see https://developer.android.google.cn/jetpack
    // see AndroidX version update https://developer.android.google.cn/jetpack/androidx/releases
    const val constraintlayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val cardview = "androidx.cardview:cardview:${Versions.cardview}"
    const val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"

    const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    const val kotlin_gradle_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofit_rxjava = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"

    const val navigation_runtime = "androidx.navigation:navigation-runtime:${Versions.navigation}"
    const val navigation_runtime_ktx =
        "androidx.navigation:navigation-runtime-ktx:${Versions.navigation}"
    const val navigation_fragment = "androidx.navigation:navigation-fragment:${Versions.navigation}"
    const val navigation_fragment_ktx =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigation_testing = "androidx.navigation:navigation-testing:${Versions.navigation}"
    const val navigation_ui = "androidx.navigation:navigation-ui:${Versions.navigation}"
    const val navigation_ui_ktx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val navigation_safe_args_plugin =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
}
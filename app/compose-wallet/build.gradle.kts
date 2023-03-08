plugins {
    id("smallraw.android.application")
    id("smallraw.android.application.compose")
    id("smallraw.android.application.flavors")
    id("smallraw.android.hilt")
    id("smallraw.android.room")
}

android {
    namespace = "com.smallraw.chain.wallet"

    //新版 ndk 写法 ndkVersion "major.minor.build"
    ndkVersion = "23.1.7779620"

    defaultConfig {
        applicationId = "com.smallraw.chain.wallet"
        versionCode = 5
        versionName = "0.0.1"

        // Custom test runner to set up Hilt dependency graph
        testInstrumentationRunner =
            "com.smallraw.apps.chain.core.testing.AppTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // abi related please see https://developer.android.com/ndk/guides/abis
        ndk {
            abiFilters += listOf("arm64-v8a")
        }
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }

    sourceSets {
        getByName("main").res.srcDirs(
            listSubFile(projectDir.absolutePath + "/src/main/res/layouts")
        )
    }

    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.android.material.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.timber)

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlin.stdlib)


    // Lifecycle
    val lifecycle_version = "2.5.1"
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    // Saved state module for ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version")
    // optional - helpers for implementing LifecycleOwner in a Service
    implementation("androidx.lifecycle:lifecycle-service:$lifecycle_version")
    // optional - ProcessLifecycleOwner provides a lifecycle for the whole application process
    implementation("androidx.lifecycle:lifecycle-process:$lifecycle_version")
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    // ViewModel - Compose support
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    // Lifecycles only (without ViewModel or LiveData)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")


    // ktx android
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.activity:activity-ktx:1.6.1")
    implementation("androidx.fragment:fragment-ktx:1.5.5")
    implementation("androidx.collection:collection-ktx:1.2.0")


    // see https://developer.android.com/jetpack/compose/setup
    val compose = "1.3.1"
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("androidx.compose.ui:ui:${compose}")
    implementation("androidx.compose.ui:ui-tooling-preview:${compose}")
    implementation("androidx.compose.material3:material3:1.0.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${compose}")
    debugImplementation("androidx.compose.ui:ui-tooling:${compose}")
    debugImplementation("androidx.compose.ui:ui-test-manifest:${compose}")
    implementation("androidx.compose.runtime:runtime-livedata:${compose}")
    implementation(libs.androidx.hilt.navigation.compose)

    // see https://developer.android.com/jetpack/compose/navigation
    val nav_version = "2.5.3"
    implementation("androidx.navigation:navigation-compose:$nav_version")

    // see https://developer.android.com/topic/performance/jankstats
    implementation("androidx.metrics:metrics-performance:1.0.0-alpha03")

    // see https://google.github.io/accompanist/systemuicontroller/
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.28.0")

    implementation("androidx.recyclerview:recyclerview:1.2.1")
    // see https://github.com/CymChad/BaseRecyclerViewAdapterHelper
    implementation("com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.4")

    implementation(project(":chain:bitcoin"))
    implementation(project(":core:authority-ckeck"))

    implementation(project(":flag:feature"))
    debugImplementation(project(":flag:feature-kit"))

    // see https://github.com/square/leakcanary/
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.9.1")

    // see https://github.com/cashapp/contour
    implementation("app.cash.contour:contour:1.1.0")
    // see https://github.com/coil-kt/coil
    implementation("io.coil-kt:coil:2.2.2")
    implementation("io.coil-kt:coil-compose:2.2.2")

    // see https://github.com/valentinilk/compose-shimmer
    implementation("com.valentinilk.shimmer:compose-shimmer:1.0.3")

    // see https://github.com/square/moshi
    implementation("com.squareup.moshi:moshi:1.13.0")
    // kapt("com.squareup.moshi:moshi-kotlin-codegen:1.12.0")
    // see https://github.com/ZacSweers/MoshiX/tree/main/moshi-ksp
    ksp("dev.zacsweers.moshix:moshi-ksp:0.14.1")

    // see https://github.com/PureWriter/FullDraggableDrawer
    implementation("com.drakeet.drawer:drawer:1.0.3")
    // Optional: No need if you just use the FullDraggableHelper
    implementation("androidx.drawerlayout:drawerlayout:1.1.1")

    // see https://github.com/X1nto/OverlappingPanelsCompose
    implementation("com.github.X1nto:OverlappingPanelsCompose:1.2.0")
    // see https://github.com/discord/OverlappingPanels
    // implementation 'com.github.discord:OverlappingPanels:0.1.5'

    androidTestImplementation(project(":core:testing"))
}

// 获取所有子文件夹(不包含当前文件夹)
fun listSubFile(resFolder: String): ArrayList<String> {
    val files = File(resFolder).listFiles()
    val folders = ArrayList<String>()

    files?.forEach { item ->
        if (item.isDirectory) {
            folders.add(item.path)
        }
    }
    folders.add(File(resFolder).parentFile.absolutePath)
    return folders
}

// androidx.test is forcing JUnit, 4.12. This forces it to use 4.13
configurations.configureEach {
    resolutionStrategy {
        force(libs.junit4)
        // Temporary workaround for https://issuetracker.google.com/174733673
        force("org.objenesis:objenesis:2.6")
    }
}

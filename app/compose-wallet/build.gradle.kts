plugins {
    id("smallraw.android.application")
    id("smallraw.android.application.compose")
    id("smallraw.android.application.flavors")
    id("smallraw.android.hilt")
    id("smallraw.android.room")
}

android {
    namespace = "com.smallraw.chain.wallet"

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
            //noinspection ChromeOsAbiSupport
            abiFilters += listOf("arm64-v8a", "x86-64", "armeabi-v7a", "x86")
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

    packaging {
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
    implementation(libs.androidx.recyclerview)
    implementation(libs.cymChad.recyclerViewAdapter)
    implementation(libs.timber)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlin.stdlib)

    // Lifecycle
    implementation(libs.bundles.androidx.lifecycle)
    implementation(libs.bundles.androidx.lifecycle.ktx)
    implementation(libs.bundles.androidx.lifecycle.compose)

    // ktx android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.collection.ktx)
    implementation(libs.androidx.metrics)

    // see https://developer.android.com/jetpack/compose/setup
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.material3)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.testManifest)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.ui.util)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.runtime.livedata)


    // see https://google.github.io/accompanist/systemuicontroller/
    implementation(libs.accompanist.systemuicontroller)

    implementation(project(":chain:bitcoin"))
    implementation(project(":core:authority-ckeck"))

    implementation(project(":flag:feature"))
    debugImplementation(project(":flag:feature-kit"))

    debugImplementation(libs.squareup.leakcanary)

    implementation(libs.contour.contour)

    implementation(libs.bundles.coil)
    implementation(libs.bundles.coil.compose)

    // see https://github.com/valentinilk/compose-shimmer
    implementation(libs.shimmer.compose)

    // see https://github.com/square/moshi
    implementation(libs.moshi)
    // kapt("com.squareup.moshi:moshi-kotlin-codegen:1.12.0")
    ksp(libs.moshi.ksp)

    implementation(libs.drakeet.drawer)
    // Optional: No need if you just use the FullDraggableHelper
    implementation(libs.androidx.drawerlayout)

    implementation(libs.x1nto.overlappingPanels.compose)

    testImplementation(project(":core:testing"))
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

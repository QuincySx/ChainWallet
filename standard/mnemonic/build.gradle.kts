plugins {
    id("smallraw.android.library")
}

android {
    namespace = "com.smallraw.wallet.mnemonic"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.android.material.material)

    api(project(":crypto:core"))
}
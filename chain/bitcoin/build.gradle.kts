plugins {
    id("smallraw.android.library")
}

android {
    namespace = "com.smallraw.chain.bitcoin"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(project(":chain:bitcoin-core"))
    implementation(project(":crypto:secp256k1"))
}
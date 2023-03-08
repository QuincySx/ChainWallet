plugins {
    id("smallraw.android.library")
}

android {
    namespace = "com.smallraw.chain.bitcoincore"
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.android.material.material)

    api(project(":crypto:core"))
    implementation(project(":crypto:secp256k1"))
}
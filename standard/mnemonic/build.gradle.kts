plugins {
    id("smallraw.android.library")
    id("smallraw.android.library.native")
}

android {
    namespace = "com.smallraw.wallet.mnemonic"
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.android.material.material)

    api(project(":crypto:core"))
}
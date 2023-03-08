plugins {
    id("smallraw.android.library")
}

android {
    namespace = "com.smallraw.chain.eos"
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.android.material.material)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.jackson)
    implementation(libs.okhttp.logging)

    api(project(":crypto:core"))
    implementation(project(":crypto:secp256k1"))
}
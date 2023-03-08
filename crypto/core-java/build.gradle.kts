plugins {
    id("smallraw.android.library")
}

android {
    namespace = "com.smallraw.chain.lib"
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.android.material.material)

    // https://mvnrepository.com/artifact/com.madgag.spongycastle/core
    api(libs.spongycastle.core)
    // https://mvnrepository.com/artifact/com.madgag.spongycastle/prov
    api(libs.spongycastle.prov)
}
plugins {
    id("smallraw.android.library")
}

android {
    namespace = "com.smallraw.lib.featureflag"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.android.material.material)
}
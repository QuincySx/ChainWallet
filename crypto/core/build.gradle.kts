plugins {
    id("smallraw.android.library")
    id("smallraw.android.library.native")
}

android {
    namespace = "com.smallraw.crypto.core"
}

dependencies {
    implementation(libs.kotlin.stdlib)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
}
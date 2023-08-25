plugins {
    id("smallraw.android.library")
    id("smallraw.android.library.native")
}

android {
    namespace = "com.smallraw.crypto.core"
}

dependencies {
    implementation(libs.kotlin.stdlib)

    implementation(libs.bundles.androidx.lifecycle.ktx)
}
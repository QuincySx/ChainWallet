plugins {
    id("smallraw.android.library")
    id("smallraw.android.library.native")
}

android {
    namespace = "com.smallraw.crypto"
}

dependencies {
    implementation(libs.kotlin.stdlib)

    implementation(project(":crypto:core"))
}
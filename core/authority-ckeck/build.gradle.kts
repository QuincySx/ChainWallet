plugins {
    id("smallraw.android.library")
    id("smallraw.android.library.native")
}

android {
    namespace = "com.smallraw.authority"
    defaultConfig {
        externalNativeBuild {
            cmake {
                cFlags += "-std=gnu99 -frtti -fexceptions"
                cppFlags += "-std=c++11 -frtti -fexceptions"
            }
        }
    }
    externalNativeBuild {
        cmake {
            path = file("CMakeLists.txt")
        }
    }
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.android.material.material)
}
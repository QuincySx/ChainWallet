plugins {
    id("smallraw.jvm.library")
}

dependencies {
    implementation(libs.kotlin.stdlib)

    // https://mvnrepository.com/artifact/com.madgag.spongycastle/core
    api(libs.spongycastle.core)
    // https://mvnrepository.com/artifact/com.madgag.spongycastle/prov
    api(libs.spongycastle.prov)
}
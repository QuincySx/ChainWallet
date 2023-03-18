pluginManagement {
    includeBuild("build-logic")

    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven {
            setUrl("https://jitpack.io")
        }
        maven {
            setUrl("https://oss.sonatype.org/content/repositories/snapshots/")
        }
        // kotlin beta version
        // maven { setUrl("https://dl.bintray.com/kotlin/kotlin-eap") }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
        maven {
            setUrl("https://oss.sonatype.org/content/repositories/snapshots/")
        }
    }
}

rootProject.name = "ChainWallet"
//includeBuild "VersionPlugin'

include(":app:compose-wallet")

include(":standard:hd-derivation")
include(":standard:mnemonic")

include(":chain:eos")
include(":chain:ethereum")
include(":chain:bitcoin-core")
include(":chain:bitcoin")

include(":crypto:core-java")
include(":crypto:ed25519")
include(":crypto:secp256k1")
include(":crypto:core")
include(":crypto:aes")

include(":core:authority-ckeck")
include(":core:testing")

include(":flag:feature-kit")
include(":flag:feature")

//include(":lib-multiplatform-crypto")

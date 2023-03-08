package com.smallraw.kmm.crypto

class PlatformInfo {
    private val platform: Platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}
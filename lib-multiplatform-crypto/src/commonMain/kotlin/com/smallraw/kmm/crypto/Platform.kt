package com.smallraw.kmm.crypto

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
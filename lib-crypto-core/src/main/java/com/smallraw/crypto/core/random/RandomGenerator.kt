package com.smallraw.crypto.core.random

import java.util.*

object RandomGenerator {
    fun getRandom(): Random {
        return AndroidRandom()
    }
}
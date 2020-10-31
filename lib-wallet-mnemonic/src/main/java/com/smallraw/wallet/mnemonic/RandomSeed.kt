package com.smallraw.wallet.mnemonic

import java.security.SecureRandom
import java.util.*

object RandomSeed {
    @JvmOverloads
    fun random(words: WordCount, random: Random = SecureRandom()): ByteArray {
        val randomSeed = ByteArray(words.byteLength())
        random.nextBytes(randomSeed)
        return randomSeed
    }
}
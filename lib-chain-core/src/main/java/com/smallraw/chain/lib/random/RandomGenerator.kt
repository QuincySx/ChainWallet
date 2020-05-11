package com.smallraw.chain.lib.random

import java.security.SecureRandom
import java.util.*

object RandomGenerator {
    // 安全但是慢
    // private val random = SecureRandom.getInstance("NativePRNG")

    // 没有 NativePRNG 安全但是快
    private val random = SecureRandom.getInstance("SHA1PRNG")

    // 默认规则
    // private val random = SecureRandom()

    fun getRandom(): Random = random
}
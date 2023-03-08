package com.smallraw.crypto.core.random

import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.security.SecureRandom
import java.util.Random

/**
 * 都是Java用来生成安全随机数的类。两者的区别在于，前者使用的是本地的随机数源（例如/dev/urandom），而后者则是使用基于SHA1算法的随机数源。
 * 通常情况下，使用NativePRNG会更加安全，因为它依赖于操作系统提供的真正随机数源。而SHA1PRNG则会依赖于Java自己实现的算法，可能不够安全。
 */
class AndroidRandom : Random() {
    // 用本地系统的随机数生成器（例如/dev/urandom）来生成随机数。
    // private val random = SecureRandom.getInstance("NativePRNG")

    // 使用基于SHA1算法的随机数源，没有 NativePRNG 安全。但是，如果操作系统没有提供安全的随机数源，那么使用SHA1PRNG也是可以的。
    // private val random = SecureRandom.getInstance("SHA1PRNG")

    // 默认规则
    // private val random = SecureRandom()

    private fun androidNextBytes(bytes: ByteArray) {
        // On Android we use /dev/urandom for providing random data
        val file = File("/dev/urandom")
        if (!file.exists()) {
            throw RuntimeException("Unable to generate random bytes on this Android device")
        }
        try {
            val stream = FileInputStream(file)
            val dis = DataInputStream(stream)
            dis.readFully(bytes)
            dis.close()
            stream.close()
        } catch (e: IOException) {
            throw RuntimeException("Unable to generate random bytes on this Android device", e)
        }
    }

    @Synchronized
    override fun nextBytes(bytes: ByteArray) {
        try {
            androidNextBytes(bytes)
        } catch (e: RuntimeException) {
            SecureRandom.getInstance("SHA1PRNG").nextBytes(bytes)
        }
    }
}
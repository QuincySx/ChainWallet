package com.smallraw.crypto.core.random

import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.security.SecureRandom
import java.util.*

class AndroidRandom : Random() {
    // 安全但是慢
    // private val random = SecureRandom.getInstance("NativePRNG")

    // 没有 NativePRNG 安全但是快
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
package com.smallraw.crypto.core.random

import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class AndroidRandom {
    @Synchronized
    fun nextBytes(bytes: ByteArray) {
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
}
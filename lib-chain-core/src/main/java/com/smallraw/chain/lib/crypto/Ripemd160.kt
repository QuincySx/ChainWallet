package com.smallraw.chain.lib.crypto

import com.smallraw.chain.lib.jni.CryptoJNI

object Ripemd160 {
    fun ripemd160(byteArray: ByteArray) = CryptoJNI.ripemd160(byteArray, byteArray.size)

    fun hash160(byteArray: ByteArray): ByteArray? {
        return Sha256.sha256(byteArray)?.let { ripemd160(it) }
    }
}
package com.smallraw.chain.lib.crypto

import com.smallraw.chain.lib.jni.CryptoJNI

object Sha3 {
    fun sha256(byteArray: ByteArray) = CryptoJNI.sha3_256(byteArray, byteArray.size)

    fun doubleSha256(byteArray: ByteArray) = CryptoJNI.doubleSha3_256(byteArray, byteArray.size)
}
package com.smallraw.chain.lib.core.crypto

import com.smallraw.chain.lib.core.execptions.JNICallException
import com.smallraw.chain.lib.core.jni.CryptoJNI

object Sha3 {
    fun sha256(byteArray: ByteArray) =
        CryptoJNI().sha3_256(byteArray, byteArray.size) ?: throw JNICallException()

    fun doubleSha256(byteArray: ByteArray) =
        CryptoJNI().doubleSha3_256(byteArray, byteArray.size) ?: throw JNICallException()
}
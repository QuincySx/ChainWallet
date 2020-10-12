package com.smallraw.crypto.core.crypto

import com.smallraw.crypto.core.execptions.JNICallException
import com.smallraw.crypto.core.jni.CryptoJNI

object Sha3 {
    fun sha256(byteArray: ByteArray) =
        CryptoJNI().sha3_256(byteArray, byteArray.size) ?: throw JNICallException()

    fun doubleSha256(byteArray: ByteArray) =
        CryptoJNI().doubleSha3_256(byteArray, byteArray.size) ?: throw JNICallException()
}
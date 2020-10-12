package com.smallraw.crypto.core.crypto

import com.smallraw.crypto.core.execptions.JNICallException
import com.smallraw.crypto.core.jni.CryptoJNI

object Keccak {
    fun sha256(byteArray: ByteArray) =
        CryptoJNI().keccak_256(byteArray, byteArray.size) ?: throw JNICallException()

    fun doubleSha256(byteArray: ByteArray) =
        CryptoJNI().doubleKeccak_256(byteArray, byteArray.size) ?: throw JNICallException()
}
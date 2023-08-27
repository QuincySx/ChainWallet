package com.smallraw.crypto.core.crypto

import com.smallraw.crypto.core.execptions.JNICallException
import com.smallraw.crypto.core.jni.CryptoJNI

object Sha256 {
    fun sha256(byteArray: ByteArray) = CryptoJNI().sha256(byteArray) ?: throw JNICallException()

    fun doubleSha256(
        byteArray: ByteArray,
    ) = CryptoJNI().doubleSha256(byteArray) ?: throw JNICallException()
}
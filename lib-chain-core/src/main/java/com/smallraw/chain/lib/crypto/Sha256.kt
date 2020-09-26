package com.smallraw.chain.lib.crypto

import com.smallraw.chain.lib.execptions.JNICallException
import com.smallraw.chain.lib.jni.CryptoJNI

object Sha256 {
    fun sha256(byteArray: ByteArray, size: Int = byteArray.size) =
        CryptoJNI().sha256(byteArray, size) ?: throw JNICallException()

    fun doubleSha256(
        byteArray: ByteArray,
        size: Int = byteArray.size
    ) =
        CryptoJNI().doubleSha256(byteArray, size) ?: throw JNICallException()
}
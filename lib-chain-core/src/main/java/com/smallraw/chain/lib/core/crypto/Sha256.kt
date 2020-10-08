package com.smallraw.chain.lib.core.crypto

import com.smallraw.chain.lib.core.execptions.JNICallException
import com.smallraw.chain.lib.core.jni.CryptoJNI

object Sha256 {
    fun sha256(byteArray: ByteArray, size: Int = byteArray.size) =
        CryptoJNI().sha256(byteArray, size) ?: throw JNICallException()

    fun doubleSha256(
        byteArray: ByteArray,
        size: Int = byteArray.size
    ) =
        CryptoJNI().doubleSha256(byteArray, size) ?: throw JNICallException()
}
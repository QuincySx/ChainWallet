package com.smallraw.crypto.core.crypto

import com.smallraw.crypto.core.execptions.JNICallException
import com.smallraw.crypto.core.jni.CryptoJNI

object Sha3 {
    fun sha224(byteArray: ByteArray) =
        CryptoJNI().sha3_224(byteArray) ?: throw JNICallException()

    fun sha256(byteArray: ByteArray) =
        CryptoJNI().sha3_256(byteArray) ?: throw JNICallException()

    fun sha384(byteArray: ByteArray) =
        CryptoJNI().sha3_384(byteArray) ?: throw JNICallException()

    fun sha512(byteArray: ByteArray) =
        CryptoJNI().sha3_512(byteArray) ?: throw JNICallException()

    fun doubleSha256(byteArray: ByteArray) =
        CryptoJNI().doubleSha3_256(byteArray) ?: throw JNICallException()
}
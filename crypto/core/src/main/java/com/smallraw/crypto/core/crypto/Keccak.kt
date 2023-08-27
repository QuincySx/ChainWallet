package com.smallraw.crypto.core.crypto

import com.smallraw.crypto.core.execptions.JNICallException
import com.smallraw.crypto.core.jni.CryptoJNI

object Keccak {
    fun sha224(byteArray: ByteArray) =
        CryptoJNI().keccak_224(byteArray) ?: throw JNICallException()

    fun sha256(byteArray: ByteArray) =
        CryptoJNI().keccak_256(byteArray) ?: throw JNICallException()

    fun sha384(byteArray: ByteArray) =
        CryptoJNI().keccak_384(byteArray) ?: throw JNICallException()

    fun sha512(byteArray: ByteArray) =
        CryptoJNI().keccak_512(byteArray) ?: throw JNICallException()

    fun doubleSha256(byteArray: ByteArray) =
        CryptoJNI().doubleKeccak_256(byteArray) ?: throw JNICallException()
}
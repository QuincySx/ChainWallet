package com.smallraw.crypto.core.crypto

import com.smallraw.crypto.core.execptions.JNICallException
import com.smallraw.crypto.core.jni.CryptoJNI

object HmacSha2 {
    fun sha256(key: ByteArray, message: ByteArray) =
        CryptoJNI().hmac_sha256(key, message, key.size, message.size) ?: throw JNICallException()

    fun sha512(key: ByteArray, message: ByteArray) =
        CryptoJNI().hmac_sha512(key, message, key.size, message.size) ?: throw JNICallException()
}
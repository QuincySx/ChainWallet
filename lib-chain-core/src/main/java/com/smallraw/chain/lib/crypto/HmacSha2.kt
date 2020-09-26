package com.smallraw.chain.lib.crypto

import com.smallraw.chain.lib.execptions.JNICallException
import com.smallraw.chain.lib.jni.CryptoJNI

object HmacSha2 {
    fun sha256(key: ByteArray, message: ByteArray) =
        CryptoJNI().hmac_sha256(key, message, key.size, message.size) ?: throw JNICallException()

    fun sha512(key: ByteArray, message: ByteArray) =
        CryptoJNI().hmac_sha512(key, message, key.size, message.size) ?: throw JNICallException()
}
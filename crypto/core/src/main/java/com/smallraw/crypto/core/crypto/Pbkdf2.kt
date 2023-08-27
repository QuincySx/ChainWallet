package com.smallraw.crypto.core.crypto

import com.smallraw.crypto.core.execptions.JNICallException
import com.smallraw.crypto.core.jni.CryptoJNI

object Pbkdf2 {
    fun hmacSha256(
        password: ByteArray,
        salt: ByteArray,
        iterations: Int = 2048,
        bitCount: Int = 256,
    ) = CryptoJNI().pbkdf2_hmac_sha256(password, salt, iterations, bitCount / 8) ?: throw JNICallException()

    fun hmacSha512(
        password: ByteArray,
        salt: ByteArray,
        iterations: Int = 2048,
        bitCount: Int = 512,
    ) = CryptoJNI().pbkdf2_hmac_sha512(password, salt, iterations, bitCount / 8) ?: throw JNICallException()
}
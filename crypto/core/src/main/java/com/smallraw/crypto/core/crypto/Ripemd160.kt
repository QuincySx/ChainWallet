package com.smallraw.crypto.core.crypto

import com.smallraw.crypto.core.execptions.JNICallException
import com.smallraw.crypto.core.jni.CryptoJNI

object Ripemd160 {
    fun ripemd160(byteArray: ByteArray) =
        CryptoJNI().ripemd160(byteArray) ?: throw JNICallException()

    fun hash160(byteArray: ByteArray): ByteArray {
        return ripemd160(Sha256.sha256(byteArray))
    }
}
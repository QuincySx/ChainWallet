package com.smallraw.chain.lib.core.crypto

import com.smallraw.chain.lib.core.execptions.JNICallException
import com.smallraw.chain.lib.core.jni.CryptoJNI

object Ripemd160 {
    fun ripemd160(byteArray: ByteArray) =
        CryptoJNI().ripemd160(byteArray, byteArray.size) ?: throw JNICallException()

    fun hash160(byteArray: ByteArray): ByteArray {
        return ripemd160(Sha256.sha256(byteArray))
    }
}
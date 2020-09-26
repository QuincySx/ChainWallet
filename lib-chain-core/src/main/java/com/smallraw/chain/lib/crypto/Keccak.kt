package com.smallraw.chain.lib.crypto

import com.smallraw.chain.lib.execptions.JNICallException
import com.smallraw.chain.lib.jni.CryptoJNI

object Keccak {
    fun sha256(byteArray: ByteArray) =
        CryptoJNI().keccak_256(byteArray, byteArray.size) ?: throw JNICallException()

    fun doubleSha256(byteArray: ByteArray) =
        CryptoJNI().doubleKeccak_256(byteArray, byteArray.size) ?: throw JNICallException()
}
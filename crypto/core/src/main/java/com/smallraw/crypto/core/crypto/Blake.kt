package com.smallraw.crypto.core.crypto

import com.smallraw.crypto.core.execptions.JNICallException
import com.smallraw.crypto.core.jni.CryptoJNI

enum class BLAKE2B(val outlen: Int) {
    BLAKE2B_256(32),
    BLAKE2B_384(48),
    BLAKE2B_512(64),
}

enum class BLAKE2S(val outlen: Int) {
    BLAKE2S_128(16),
    BLAKE2S_256(32),
}

object Blake {
    fun blake256(byteArray: ByteArray) = CryptoJNI().blake_256(byteArray) ?: throw JNICallException()

    fun blake2b(byteArray: ByteArray, keyArray: ByteArray? = null, outlen: BLAKE2B): ByteArray {
        return if (keyArray != null) {
            CryptoJNI().blake2bWithKey(byteArray, keyArray, outlen.outlen) ?: throw JNICallException()
        } else {
            CryptoJNI().blake2b(byteArray, outlen.outlen) ?: throw JNICallException()
        }
    }

    fun blake2s(byteArray: ByteArray, keyArray: ByteArray?= null, outlen: BLAKE2S): ByteArray {
        return if (keyArray != null) {
            CryptoJNI().blake2sWithKey(byteArray, keyArray, outlen.outlen) ?: throw JNICallException()
        } else {
            CryptoJNI().blake2s(byteArray, outlen.outlen) ?: throw JNICallException()
        }
    }
}
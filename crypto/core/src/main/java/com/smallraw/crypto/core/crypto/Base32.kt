package com.smallraw.crypto.core.crypto

import com.smallraw.crypto.core.execptions.JNICallException
import com.smallraw.crypto.core.jni.CryptoJNI

object Base32 {
    fun encode(byteArray: ByteArray, padding: Boolean = true) =
        CryptoJNI().base32Encode(byteArray, padding) ?: throw JNICallException()

    fun decode(str: String, padding: Boolean = true) = CryptoJNI().base32Decode(str,padding) ?: throw JNICallException()
}
package com.smallraw.crypto.core.crypto

import com.smallraw.crypto.core.execptions.JNICallException
import com.smallraw.crypto.core.jni.CryptoJNI

object Base58 {
    fun encode(byteArray: ByteArray) =
        CryptoJNI().base58Encode(byteArray, byteArray.size) ?: throw JNICallException()

    fun decode(str: String) = CryptoJNI().base58Decode(str) ?: throw JNICallException()

    fun encodeCheck(byteArray: ByteArray) =
        CryptoJNI().base58EncodeCheck(byteArray, byteArray.size) ?: throw JNICallException()

    fun decodeCheck(str: String) =
        CryptoJNI().base58DecodeCheck(str) ?: throw JNICallException()
}
package com.smallraw.crypto.core.crypto

import com.smallraw.crypto.core.execptions.JNICallException
import com.smallraw.crypto.core.jni.CryptoJNI

object Sha512 {
    fun sha512(byteArray: ByteArray) = CryptoJNI().sha512(byteArray) ?: throw JNICallException()
}
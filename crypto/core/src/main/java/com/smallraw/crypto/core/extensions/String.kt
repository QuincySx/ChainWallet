package com.smallraw.crypto.core.extensions

import com.smallraw.crypto.core.execptions.JNICallException
import com.smallraw.crypto.core.jni.CryptoJNI

fun String.hexToByteArray(): ByteArray {
    val str = if (this.startsWith("0x", true)) {
        this.substring(2)
    } else {
        this
    }
    return CryptoJNI().strToHex(str) ?: throw JNICallException()
}
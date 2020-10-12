package com.smallraw.crypto.core.extensions

import com.smallraw.crypto.core.execptions.JNICallException
import com.smallraw.crypto.core.jni.CryptoJNI

fun ByteArray.toHex() = CryptoJNI().hexToStr(this, this.size) ?: throw JNICallException()

operator fun ByteArray.plus(byte: Byte): ByteArray {
    val thisSize = size
    val result = this.copyOf(thisSize + 1)
    result[lastIndex] = byte
    return result
}

operator fun ByteArray.plus(int: Int): ByteArray {
    val thisSize = size
    val result = this.copyOf(thisSize + 1)
    result[lastIndex] = int.and(0xFF).toByte()
    return result
}
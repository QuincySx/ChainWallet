package com.smallraw.chain.lib.extensions

import com.smallraw.chain.lib.execptions.JNICallException
import com.smallraw.chain.lib.jni.CryptoJNI

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
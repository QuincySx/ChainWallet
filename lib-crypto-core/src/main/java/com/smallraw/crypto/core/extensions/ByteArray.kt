package com.smallraw.crypto.core.extensions

import com.smallraw.crypto.core.execptions.JNICallException
import com.smallraw.crypto.core.jni.CryptoJNI
import java.math.BigInteger
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.experimental.xor

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

fun ByteArray.toShort(): Short {
    val bb = ByteBuffer.wrap(this)
    bb.order(ByteOrder.BIG_ENDIAN)

    return bb.short
}

fun ByteArray?.toInt(): Int {
    return if (this == null || this.isEmpty()) 0 else BigInteger(1, this).toInt()
}

fun ByteArray?.toLong(): Long {
    return if (this == null || this.isEmpty()) 0 else BigInteger(1, this).toLong()
}

fun ByteArray?.toBigInteger(): BigInteger {
    return if (this == null || this.isEmpty()) BigInteger.ZERO else BigInteger(1, this)
}

fun ByteArray.xor(other: ByteArray): ByteArray {
    val out = ByteArray(this.size)
    for (i in this.indices) {
        out[i] = (this[i] xor (other[i % other.size]))
    }
    return out
}
package com.smallraw.crypto.core.extensions

import java.math.BigInteger


fun BigInteger.asUnsignedByteArray(): ByteArray {
    val bytes = this.toByteArray()
    if (bytes[0] == 0.toByte()) {
        val tmp = ByteArray(bytes.size - 1)
        System.arraycopy(bytes, 1, tmp, 0, tmp.size)
        return tmp
    }
    return bytes
}

fun BigInteger.asUnsignedByteArray(length: Int): ByteArray {
    val bytes = this.toByteArray()
    if (bytes.size == length) {
        return bytes
    }
    val start = if (bytes[0] == 0.toByte()) 1 else 0
    val count = bytes.size - start
    require(count <= length) { "standard length exceeded for value" }
    val tmp = ByteArray(length)
    System.arraycopy(bytes, start, tmp, tmp.size - count, count)
    return tmp
}

fun BigInteger.toBytes(numBytes: Int): ByteArray {
    val bytes = ByteArray(numBytes)
    val biBytes = this.toByteArray()
    val start = if (biBytes.size == numBytes + 1) 1 else 0
    val length = kotlin.math.min(biBytes.size, numBytes)
    System.arraycopy(biBytes, start, bytes, numBytes - length, length)
    return bytes
}
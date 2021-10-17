package com.smallraw.crypto.core.extensions

fun Byte.toByteArray(): ByteArray {
    return ByteArray(1) { this }
}

operator fun Byte.plus(bytes: ByteArray): ByteArray {
    val arraySize = bytes.size
    val result = ByteArray(arraySize + 1)
    result[0] = this
    System.arraycopy(bytes, 0, result, 1, arraySize)
    return result
}
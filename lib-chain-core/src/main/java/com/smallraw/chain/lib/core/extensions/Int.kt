package com.smallraw.chain.lib.core.extensions

operator fun Int.plus(bytes: ByteArray): ByteArray {
    val arraySize = bytes.size
    val result = ByteArray(arraySize + 1)
    result[0] = this.and(0xFF).toByte()
    System.arraycopy(bytes, 0, result, 1, arraySize)
    return result
}
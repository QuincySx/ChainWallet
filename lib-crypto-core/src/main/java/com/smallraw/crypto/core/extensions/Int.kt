package com.smallraw.crypto.core.extensions

operator fun Int.plus(bytes: ByteArray): ByteArray {
    val arraySize = bytes.size
    val result = ByteArray(arraySize + 1)
    result[0] = this.and(0xFF).toByte()
    System.arraycopy(bytes, 0, result, 1, arraySize)
    return result
}

/**
 * 大端序
 */
fun Int.toByteArray() = ByteArray(4) { i ->
    shr(8 * (3 - i)).toByte()
}

/**
 * 小端序
 */
fun Int.toMinimalByteArray() = toByteArray().let {
    it.copyOfRange(it.minimalStart(), 4)
}

private fun ByteArray.minimalStart() =
    indexOfFirst { it != 0.toByte() }.let { if (it == -1) 4 else it }

fun Int.toBytesNoLeadZeroes(): ByteArray {
    var value = this

    if (value == 0) return byteArrayOf()

    var length = 0

    var tmpVal = value
    while (tmpVal != 0) {
        tmpVal = tmpVal.ushr(8)
        ++length
    }

    val result = ByteArray(length)

    var index = result.size - 1
    while (value != 0) {

        result[index] = (value and 0xFF).toByte()
        value = value.ushr(8)
        index -= 1
    }

    return result
}
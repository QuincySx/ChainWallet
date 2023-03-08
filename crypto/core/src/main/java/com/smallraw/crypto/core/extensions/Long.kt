package com.smallraw.crypto.core.extensions

fun Long.toByteArray(): ByteArray {
    var array = this.toBigInteger().toByteArray()
    if (array[0].toInt() == 0) {
        val tmp = ByteArray(array.size - 1)
        System.arraycopy(array, 1, tmp, 0, tmp.size)
        array = tmp
    }
    return array
}
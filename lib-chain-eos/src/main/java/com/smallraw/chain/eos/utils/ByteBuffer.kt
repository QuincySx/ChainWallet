package com.smallraw.chain.eos.utils

class ByteBuffer {
    var buffer = byteArrayOf()
    fun concat(b: ByteArray?) {
        buffer = ByteUtils.concat(buffer, b)
    }
}
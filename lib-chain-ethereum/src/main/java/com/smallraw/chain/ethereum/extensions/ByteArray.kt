package com.smallraw.chain.ethereum.extensions

import com.smallraw.crypto.core.extensions.toHex
import com.smallraw.crypto.core.stream.ByteWriterStream

fun ByteArray?.toHexPrefix(): String {
    return this?.toHex()?.compleHexPrefix() ?: ""
}

fun Array<ByteArray?>.merge(): ByteArray {
    val byteWriterStream = ByteWriterStream(64)
    this.forEach {
        it?.let {
            byteWriterStream.writeBytes(it)
        }
    }
    return byteWriterStream.toBytes()
}

fun ByteArray.removeLeadingZero() = if (first() == 0.toByte()) copyOfRange(1, size) else this
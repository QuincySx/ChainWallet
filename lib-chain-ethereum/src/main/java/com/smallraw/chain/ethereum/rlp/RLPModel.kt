package com.smallraw.chain.ethereum.rlp

import com.smallraw.chain.ethereum.extensions.removeLeadingZero
import com.smallraw.crypto.core.extensions.toMinimalByteArray
import java.math.BigInteger

internal const val ELEMENT_OFFSET = 128
internal const val LIST_OFFSET = 192

sealed class RLPType

data class RLPItem(val bytes: ByteArray) : RLPType() {

    override fun equals(other: Any?) = when (other) {
        is RLPItem -> bytes.contentEquals(other.bytes)
        else -> false
    }

    override fun hashCode() = bytes.contentHashCode()
}

data class RLPList(val element: List<RLPType>) : RLPType() {
    operator fun get(index: Int) = element[index]
    fun getOrNull(index: Int) = element.getOrNull(index)
}


// to RLP
fun String.toRLP() = RLPItem(toByteArray())
fun Int.toRLP() = RLPItem(toMinimalByteArray())
fun Long.toRLP() = RLPItem(toBigInteger().toByteArray().removeLeadingZero())
fun BigInteger.toRLP() = RLPItem(toByteArray().removeLeadingZero())
fun ByteArray?.toRLP() = RLPItem(this ?: byteArrayOf())
fun Byte.toRLP() = RLPItem(ByteArray(1) { this })
fun List<RLPType>.toRLP() = RLPList(this)

// from RLP
fun RLPItem.toIntFromRLP() = if (bytes.isEmpty()) {
    0
} else {
    bytes.mapIndexed { index, byte -> (byte.toInt() and 0xff).shl((bytes.size - 1 - index) * 8) }
        .reduce { acc, i -> acc + i }
}

fun RLPItem.toBigIntegerFromRLP(): BigInteger =
    if (bytes.isEmpty()) BigInteger.ZERO else BigInteger(1, bytes)

fun RLPItem.toLongFromRLP(): Long = if (bytes.isEmpty()) 0 else BigInteger(1, bytes).toLong()
fun RLPItem.toByteFromRLP(): Byte {
    require(bytes.size == 1) { "trying to convert RLP with != 1 byte to Byte" }
    return bytes.first()
}

fun RLPItem.toStringFromRLP() = String(bytes)

class IllegalRLPException(msg: String) : IllegalArgumentException(msg)
package com.smallraw.chain.lib.utils

private val HEX_CHARS = "0123456789ABCDEF".toCharArray()
val toHEX = { b: ByteArray ->
    buildString {
        b.forEach {
            val octet = it.toInt()
            val firstIndex = (octet and 0xF0).ushr(4)
            val secondIndex = octet and 0x0F
            append(HEX_CHARS[firstIndex])
            append(HEX_CHARS[secondIndex])
        }
    }
}

val hexToBytes = { hex: String ->
    val len = hex.length
    val result = ByteArray(len / 2)
    (0 until len step 2).forEach { i ->
        result[i.shr(1)] =
            Character.digit(hex[i], 16).shl(4).or(Character.digit(hex[i + 1], 16)).toByte()
    }
    result
}
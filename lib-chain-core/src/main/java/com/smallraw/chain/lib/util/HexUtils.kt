package com.smallraw.chain.lib.util

// 性能比较差
//fun ByteArray.toHex() = this.joinToString("") {
//    "%02x".format(it)
//}

// 性能比较差
//fun String.hexToBytes() = this.chunked(2).map { it.toInt(16).toByte() }.toByteArray()

// 参考 see https://gist.github.com/fabiomsr/845664a9c7e92bafb6fb0ca70d4e44fd

private val HEX_CHARS = "0123456789ABCDEF".toCharArray()
private val toHEX = { b: ByteArray ->
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

private val hexToBytes = { hex: String ->
    val len = hex.length
    val result = ByteArray(len / 2)
    (0 until len step 2).forEach { i ->
        result[i.shr(1)] =
            HEX_CHARS.indexOf(hex[i]).shl(4).or(HEX_CHARS.indexOf(hex[i + 1])).toByte()
    }
    result
}

fun ByteArray.toHex() = toHEX(this)

fun String.hexToBytes() = hexToBytes(this)
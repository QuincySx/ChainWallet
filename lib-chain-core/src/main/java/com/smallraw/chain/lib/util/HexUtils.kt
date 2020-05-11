package com.smallraw.chain.lib.util

// 性能比较差
//fun ByteArray.toHex() = this.joinToString("") {
//    "%02x".format(it)
//}

// 参考 see https://gist.github.com/fabiomsr/845664a9c7e92bafb6fb0ca70d4e44fd

private val HEX_CHARS = "0123456789ABCDEF".toCharArray()
fun ByteArray.toHex(): String {
    val result = StringBuffer()

    forEach {
        val octet = it.toInt()
        val firstIndex = (octet and 0xF0).ushr(4)
        val secondIndex = octet and 0x0F
        result.append(HEX_CHARS[firstIndex])
        result.append(HEX_CHARS[secondIndex])
    }

    return result.toString()
}

fun String.hexToBytes() = this.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
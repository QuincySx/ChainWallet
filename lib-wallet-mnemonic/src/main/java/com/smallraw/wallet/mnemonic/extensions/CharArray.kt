package com.smallraw.wallet.mnemonic.extensions

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream

fun CharArray.toUTF8ByteArray(): ByteArray {
    val bOut = ByteArrayOutputStream()

    try {
        this.toUTF8ByteArray(bOut)
    } catch (e: IOException) {
        throw IllegalStateException("cannot encode string to byte array!")
    }

    return bOut.toByteArray()
}

@Throws(IOException::class)
fun CharArray.toUTF8ByteArray(sOut: OutputStream) {
    var i = 0
    while (i < this.size) {
        var ch = this[i]
        if (ch.toInt() < 0x0080) {
            sOut.write(ch.toInt())
        } else if (ch.toInt() < 0x0800) {
            sOut.write(0xc0 or (ch.toInt() shr 6))
            sOut.write(0x80 or (ch.toInt() and 0x3f))
        } else if (ch.toInt() >= 0xD800 && ch.toInt() <= 0xDFFF) {
            // in error - can only happen, if the Java String class has a
            // bug.
            check(i + 1 < this.size) { "invalid UTF-16 codepoint" }
            val W1 = ch
            ch = this[++i]
            val W2 = ch
            // in error - can only happen, if the Java String class has a
            // bug.
            check(W1.toInt() <= 0xDBFF) { "invalid UTF-16 codepoint" }
            val codePoint = (W1.toInt() and 0x03FF shl 10 or (W2.toInt() and 0x03FF)) + 0x10000
            sOut.write(0xf0 or (codePoint shr 18))
            sOut.write(0x80 or (codePoint shr 12 and 0x3F))
            sOut.write(0x80 or (codePoint shr 6 and 0x3F))
            sOut.write(0x80 or (codePoint and 0x3F))
        } else {
            sOut.write(0xe0 or (ch.toInt() shr 12))
            sOut.write(0x80 or (ch.toInt() shr 6 and 0x3F))
            sOut.write(0x80 or (ch.toInt() and 0x3F))
        }
        i++
    }
}
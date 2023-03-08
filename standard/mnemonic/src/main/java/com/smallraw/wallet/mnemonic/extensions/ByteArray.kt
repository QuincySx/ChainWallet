package com.smallraw.wallet.mnemonic.extensions

import kotlin.experimental.or

fun ByteArray.next11Bits(offset: Int): Int {
    val skip = offset / 8
    val lowerBitsToRemove = (3 * 8 - 11) - offset % 8
    return ((this[skip].toInt() and 0xff shl 16) or
            (this[skip + 1].toInt() and 0xff shl 8) or
            if (lowerBitsToRemove < 8) {
                this[skip + 2].toInt() and 0xff
            } else {
                0
            }) shr lowerBitsToRemove and (1 shl 11) - 1
}

fun ByteArray.writeNext11(value: Int, offset: Int) {
    val skip = offset / 8
    val bitSkip = offset % 8
    run {
        //byte 0
        val firstValue = this[skip]
        val toWrite = (value shr (3 + bitSkip)).toByte()
        this[skip] = (firstValue or toWrite)
    }
    run {
        //byte 1
        val valueInByte = this[skip + 1]
        val i = 5 - bitSkip
        val toWrite = (if (i > 0) {
            value shl i
        } else {
            value shr -i
        }).toByte()
        this[skip + 1] = (valueInByte or toWrite) as Byte
    }
    if (bitSkip >= 6) { //byte 2
        val valueInByte = this[skip + 2]
        val toWrite = (value shl 13 - bitSkip).toByte()
        this[skip + 2] = (valueInByte or toWrite)
    }
}
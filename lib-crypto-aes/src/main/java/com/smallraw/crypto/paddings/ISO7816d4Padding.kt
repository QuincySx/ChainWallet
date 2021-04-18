package com.smallraw.crypto.paddings

import java.security.SecureRandom


/**
 * A padder that adds ISO10126-2 padding to a block.
 */
class ISO7816d4Padding : BlockCipherPadding {
    var random: SecureRandom? = null

    /**
     * Initialise the padder.
     *
     * @param random - a SecureRandom if available.
     */
    @Throws(IllegalArgumentException::class)
    override fun init(random: SecureRandom?) {
        // nothing to do.
    }

    /**
     * Return the name of the algorithm the padder implements.
     *
     * @return the name of the algorithm the padder implements.
     */
    override val paddingName: String
        get() = "ISO7816-4"

    /**
     * add the pad bytes to the passed in block, returning the
     * number of bytes added.
     */
    override fun addPadding(
        inputByte: ByteArray,
        inOff: Int
    ): Int {
        var inOff = inOff
        val added = inputByte.size - inOff
        inputByte[inOff] = 0x80.toByte()
        inOff++
        while (inOff < inputByte.size) {
            inputByte[inOff] = 0.toByte()
            inOff++
        }
        return added
    }

    /**
     * return the number of pad bytes present in the block.
     */
    @Throws(IllegalArgumentException::class)
    override fun padCount(inputByte: ByteArray): Int {
        var count = inputByte.size - 1
        while (count > 0 && inputByte[count] == 0.toByte()) {
            count--
        }
        if (inputByte[count] != 0x80.toByte()) {
            throw IllegalArgumentException("pad block corrupted")
        }
        return inputByte.size - count
    }
}
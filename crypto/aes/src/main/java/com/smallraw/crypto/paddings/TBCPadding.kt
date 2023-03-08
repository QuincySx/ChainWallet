package com.smallraw.crypto.paddings

import java.security.SecureRandom


/**
 * A padder that adds Trailing-Bit-Compliment padding to a block.
 *
 *
 * This padding pads the block out with the compliment of the last bit
 * of the plain text.
 *
 */
class TBCPadding : BlockCipherPadding {
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
        get() = "TBC"

    /**
     * add the pad bytes to the passed in block, returning the
     * number of bytes added.
     *
     *
     * Note: this assumes that the last block of plain text is always
     * passed to it inside in. i.e. if inOff is zero, indicating the
     * entire block is to be overwritten with padding the value of in
     * should be the same as the last block of plain text.
     *
     */
    override fun addPadding(
        inputByte: ByteArray,
        inOff: Int
    ): Int {
        var inOff = inOff
        val count = inputByte.size - inOff
        val code: Byte = if (inOff > 0) {
            (if (inputByte[inOff - 1].toInt() and 0x01 == 0) 0xff else 0x00).toByte()
        } else {
            (if (inputByte[inputByte.size - 1].toInt() and 0x01 == 0) 0xff else 0x00).toByte()
        }
        while (inOff < inputByte.size) {
            inputByte[inOff] = code
            inOff++
        }
        return count
    }

    /**
     * return the number of pad bytes present in the block.
     */
    @Throws(IllegalArgumentException::class)
    override fun padCount(inputByte: ByteArray): Int {
        val code = inputByte[inputByte.size - 1]
        var index = inputByte.size - 1
        while (index > 0 && inputByte[index - 1] == code) {
            index--
        }
        return inputByte.size - index
    }
}
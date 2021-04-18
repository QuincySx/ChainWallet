package com.smallraw.crypto.paddings

import java.security.SecureRandom

/**
 * A padder that adds NULL byte padding to a block.
 */
class ZeroBytePadding : BlockCipherPadding {
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
    get() = "ZeroByte"

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
        var count = inputByte.size
        while (count > 0) {
            if (inputByte[count - 1].toInt() != 0) {
                break
            }
            count--
        }
        return inputByte.size - count
    }
}
package com.smallraw.crypto.paddings

import java.security.SecureRandom

/**
 * A padder that adds NULL byte padding to a block.
 */
class NoPadding : BlockCipherPadding {
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
        get() = "No Padding"

    /**
     * add the pad bytes to the passed in block, returning the
     * number of bytes added.
     */
    override fun addPadding(
        inputByte: ByteArray,
        inOff: Int
    ): Int {
        return 0
    }

    /**
     * return the number of pad bytes present in the block.
     */
    @Throws(IllegalArgumentException::class)
    override fun padCount(inputByte: ByteArray): Int {
        return 0
    }
}
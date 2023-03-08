package com.smallraw.crypto.paddings

import java.security.SecureRandom


/**
 * Block cipher padders are expected to conform to this interface
 */
interface BlockCipherPadding {
    /**
     * Initialise the padder.
     *
     * @param random the source of randomness for the padding, if required.
     */
    @Throws(IllegalArgumentException::class)
    fun init(random: SecureRandom?)

    /**
     * Return the name of the algorithm the cipher implements.
     *
     * @return the name of the algorithm the cipher implements.
     */
    val paddingName: String

    /**
     * add the pad bytes to the passed in block, returning the
     * number of bytes added.
     *
     *
     * Note: this assumes that the last block of plain text is always
     * passed to it inside in. i.e. if inOff is zero, indicating the
     * entire block is to be overwritten with padding the value of in
     * should be the same as the last block of plain text. The reason
     * for this is that some modes such as "trailing bit compliment"
     * base the padding on the last byte of plain text.
     *
     */
    fun addPadding(inputByte: ByteArray, inOff: Int): Int

    /**
     * return the number of pad bytes present in the block.
     * @exception InvalidCipherTextException if the padding is badly formed
     * or invalid.
     */
    @Throws(IllegalArgumentException::class)
    fun padCount(inputByte: ByteArray): Int
}
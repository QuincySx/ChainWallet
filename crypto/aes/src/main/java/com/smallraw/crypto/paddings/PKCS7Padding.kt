package com.smallraw.crypto.paddings

import java.security.SecureRandom


/**
 * A padder that adds PKCS7/PKCS5 padding to a block.
 */
class PKCS7Padding : BlockCipherPadding {
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
        get() = "PKCS7"

    /**
     * add the pad bytes to the passed in block, returning the
     * number of bytes added.
     */
    override fun addPadding(
        inputByte: ByteArray,
        inOff: Int
    ): Int {
        var inOff = inOff
        val code = (inputByte.size - inOff).toByte()
        while (inOff < inputByte.size) {
            inputByte[inOff] = code
            inOff++
        }
        return code.toInt()
    }

    /**
     * return the number of pad bytes present in the block.
     */
    @Throws(IllegalArgumentException::class)
    override fun padCount(inputByte: ByteArray): Int {
        val count: Int = inputByte[inputByte.size - 1].toInt().and(0xFF)
        val countAsbyte = count.toByte()

        // constant time version
        var failed = (count > inputByte.size) or (count == 0)
        for (i in inputByte.indices) {
            failed = failed or ((inputByte.size - i <= count) and (inputByte[i] != countAsbyte))
        }
        if (failed) {
            throw IllegalArgumentException("pad block corrupted")
        }
        return count
    }
}
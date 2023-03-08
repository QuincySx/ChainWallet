package com.smallraw.crypto.paddings

import java.security.SecureRandom


/**
 * A padder that adds ISO10126-2 padding to a block.
 */
class ISO10126d2Padding : BlockCipherPadding {
    var random: SecureRandom? = null

    /**
     * Initialise the padder.
     *
     * @param random a SecureRandom if available.
     */
    @Throws(IllegalArgumentException::class)
    override fun init(random: SecureRandom?) {
        if (random != null) {
            this.random = random
        } else {
            this.random = SecureRandom()
        }
    }

    /**
     * Return the name of the algorithm the padder implements.
     *
     * @return the name of the algorithm the padder implements.
     */
    override val paddingName: String
        get() = "ISO10126-2"

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
        while (inOff < inputByte.size - 1) {
            inputByte[inOff] = (random?.nextInt() as Byte?) ?: 0
            inOff++
        }
        inputByte[inOff] = code
        return code.toInt()
    }

    /**
     * return the number of pad bytes present in the block.
     */
    @Throws(IllegalArgumentException::class)
    override fun padCount(inputByte: ByteArray): Int {
        val count: Int = inputByte[inputByte.size - 1].toInt().and(0xFF)
        if (count > inputByte.size) {
            throw IllegalArgumentException("pad block corrupted")
        }
        return count
    }
}
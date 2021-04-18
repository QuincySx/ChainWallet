package com.smallraw.crypto.paddings

import java.security.SecureRandom


/**
 * A padder that adds X9.23 padding to a block - if a SecureRandom is
 * passed in random padding is assumed, otherwise padding with zeros is used.
 */
class X923Padding : BlockCipherPadding {
    var random: SecureRandom? = null

    /**
     * Initialise the padder.
     *
     * @param random a SecureRandom if one is available.
     */
    @Throws(IllegalArgumentException::class)
    override fun init(random: SecureRandom?) {
        this.random = random
    }

    /**
     * Return the name of the algorithm the padder implements.
     *
     * @return the name of the algorithm the padder implements.
     */
    override val paddingName: String
        get() = "X9.23"

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
            if (random == null) {
                inputByte[inOff] = 0
            } else {
                inputByte[inOff] = (random?.nextInt() as Byte?) ?: 0
            }
            inOff++
        }
        inputByte[inOff] = code
        return code.toInt()
    }

    /**
     * return the number of pad bytes present in the block.
     */
    @Throws(IllegalArgumentException::class)
    override fun padCount(`in`: ByteArray): Int {
        val count: Int = `in`[`in`.size - 1].toInt() and 0xff
        if (count > `in`.size) {
            throw IllegalArgumentException("pad block corrupted")
        }
        return count
    }
}
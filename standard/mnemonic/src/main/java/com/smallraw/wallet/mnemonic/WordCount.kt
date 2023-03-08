package com.smallraw.wallet.mnemonic

enum class WordCount(private val bitLength: Int) {
    /**
     * 3个助记词
     */
    THREE(27),

    /**
     * 6个助记词
     */
    SIX(54),

    /**
     * 9个助记词
     */
    NINE(72),

    /**
     * 12个助记词
     */
    TWELVE(128),

    /**
     * 15个助记词
     */
    FIFTEEN(160),

    /**
     * 18个助记词
     */
    EIGHTEEN(192),

    /**
     * 21个助记词
     */
    TWENTY_ONE(224),

    /**
     * 24个助记词
     */
    TWENTY_FOUR(256);

    open fun bitLength(): Int {
        return bitLength
    }

    open fun byteLength(): Int {
        return bitLength / 8
    }
}
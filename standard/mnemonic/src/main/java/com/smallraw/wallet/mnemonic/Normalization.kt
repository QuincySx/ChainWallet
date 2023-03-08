package com.smallraw.wallet.mnemonic

import java.text.Normalizer

/**
 * @author QuincySx
 * @date 2018/5/15 上午10:40
 */
internal object Normalization {
    fun normalizeNFKD(string: String?): String {
        return string?.let { Normalizer.normalize(string, Normalizer.Form.NFKD) } ?: ""
    }

    fun normalizeNFKD(c: Char): Char {
        return normalizeNFKD("" + c)[0]
    }
}
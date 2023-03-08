package com.smallraw.wallet.mnemonic

import java.text.Normalizer
import java.util.*

interface NFKDNormalizer {
    fun normalize(charSequence: CharSequence): String
}

class WordListMapNormalization internal constructor(wordList: WordList) : NFKDNormalizer {
    private val normalizedMap: MutableMap<CharSequence, String> = HashMap()
    override fun normalize(charSequence: CharSequence): String {
        val normalized = normalizedMap[charSequence]
        return normalized
            ?: Normalizer.normalize(
                charSequence,
                Normalizer.Form.NFKD
            )
    }

    init {
        for (i in 0 until (1 shl 11)) {
            val word = wordList.getWord(i)
            val normalized = Normalizer.normalize(word, Normalizer.Form.NFKD)
            normalizedMap[word] = normalized
            normalizedMap[normalized] = normalized
            normalizedMap[Normalizer.normalize(word, Normalizer.Form.NFC)] = normalized
        }
    }
}
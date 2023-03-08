package com.smallraw.wallet.mnemonic

import com.smallraw.wallet.mnemonic.exception.MnemonicWordException
import java.util.*

class SeedCalculatorByWordListLookUp internal constructor(
    private val seedCalculator: SeedCalculator,
    private val wordList: WordList
) {
    private val map: MutableMap<CharSequence, CharArray> = HashMap()
    private val normalizer: NFKDNormalizer

    /**
     * Calculate the seed given a mnemonic and corresponding passphrase.
     * The phrase is not checked for validity here, for that use a [MnemonicValidator].
     *
     *
     * The purpose of this method is to avoid constructing a mnemonic String if you have gathered a list of
     * words from the user and also to avoid having to normalize it, all words in the [WordList] are normalized
     * instead.
     *
     *
     * Due to normalization, the passphrase still needs to be [String], and not [CharSequence], this is an
     * open issue: https://github.com/NovaCrypto/BIP39/issues/7
     *
     * @param mnemonic   The memorable list of words, ideally selected from the word list that was supplied while creating this object.
     * @param passphrase An optional passphrase, use "" if not required
     * @return a seed for HD wallet generation
     */
    fun calculateSeed(mnemonic: Collection<CharSequence>, passphrase: String?): ByteArray {
        val words = mnemonic.size
        val chars = arrayOfNulls<CharArray>(words)
        val toClear: MutableList<CharArray> = LinkedList()
        var count = 0
        var wordIndex = 0
        for (word in mnemonic) {
            var wordChars = map[normalizer.normalize(word)]
            if (wordChars == null) {
                if (wordList.supportFirst && word.length >= 4) {
                    val wordByWordFirst = wordList.getWordByWordFirst(word)
                        ?: throw MnemonicWordException(word.toString())
                    wordChars = normalizer.normalize(wordByWordFirst).toCharArray()
                } else {
                    wordChars = normalizer.normalize(word).toCharArray()
                }
                toClear.add(wordChars)
            }
            chars[wordIndex++] = wordChars
            count += wordChars.size
        }
        count += words - 1
        val mnemonicChars = CharArray(count)
        return try {
            var index = 0
            for (i in chars.indices) {
                System.arraycopy(chars[i]!!, 0, mnemonicChars, index, chars[i]?.size ?: 0)
                index += chars[i]?.size ?: 0
                if (i < chars.size - 1) {
                    mnemonicChars[index++] = ' '
                }
            }
            seedCalculator.calculateSeed(mnemonicChars, passphrase)
        } finally {
            Arrays.fill(mnemonicChars, '\u0000')
            Arrays.fill(chars, null)
            for (charsToClear in toClear) Arrays.fill(charsToClear, '\u0000')
        }
    }

    init {
        normalizer = WordListMapNormalization(wordList)
        for (i in 0 until (1 shl 11)) {
            val word = normalizer.normalize(wordList.getWord(i))
            map[word] = word.toCharArray()
        }
    }
}
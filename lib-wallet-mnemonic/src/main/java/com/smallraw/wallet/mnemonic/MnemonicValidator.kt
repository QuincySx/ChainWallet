package com.smallraw.wallet.mnemonic

import com.smallraw.wallet.mnemonic.Normalization.normalizeNFKD
import com.smallraw.wallet.mnemonic.exception.InvalidChecksumException
import com.smallraw.wallet.mnemonic.exception.InvalidWordCountException
import com.smallraw.wallet.mnemonic.exception.UnexpectedWhiteSpaceException
import com.smallraw.wallet.mnemonic.exception.WordNotFoundException
import com.smallraw.wallet.mnemonic.extensions.writeNext11
import java.util.*
import kotlin.experimental.and
import kotlin.experimental.xor

class MnemonicValidator private constructor(wordList: WordList) {
    private val words: Array<WordAndIndex?>
    private val charSequenceSplitter: CharSequenceSplitter
    private val normalizer: NFKDNormalizer

    /**
     * Check that the supplied mnemonic fits the BIP0039 spec.
     *
     * @param mnemonic The memorable list of words
     * @throws InvalidChecksumException      If the last bytes don't match the expected last bytes
     * @throws InvalidWordCountException     If the number of words is not a multiple of 3, 24 or fewer
     * @throws WordNotFoundException         If a word in the mnemonic is not present in the word list
     * @throws UnexpectedWhiteSpaceException Occurs if one of the supplied words is empty, e.g. a double space
     */
    @Throws(
        InvalidChecksumException::class,
        InvalidWordCountException::class,
        WordNotFoundException::class,
        UnexpectedWhiteSpaceException::class
    )
    fun validate(mnemonic: CharSequence) {
        validate(charSequenceSplitter.split(mnemonic))
    }

    /**
     * Check that the supplied mnemonic fits the BIP0039 spec.
     *
     *
     * The purpose of this method overload is to avoid constructing a mnemonic String if you have gathered a list of
     * words from the user.
     *
     * @param mnemonic The memorable list of words
     * @throws InvalidChecksumException      If the last bytes don't match the expected last bytes
     * @throws InvalidWordCountException     If the number of words is not a multiple of 3, 24 or fewer
     * @throws WordNotFoundException         If a word in the mnemonic is not present in the word list
     * @throws UnexpectedWhiteSpaceException Occurs if one of the supplied words is empty
     */
    @Throws(
        InvalidChecksumException::class,
        InvalidWordCountException::class,
        WordNotFoundException::class,
        UnexpectedWhiteSpaceException::class
    )
    fun validate(mnemonic: Collection<CharSequence>) {
        val wordIndexes = findWordIndexes(mnemonic)
        try {
            validate(wordIndexes)
        } finally {
            Arrays.fill(wordIndexes, 0)
        }
    }

    @Throws(UnexpectedWhiteSpaceException::class, WordNotFoundException::class)
    private fun findWordIndexes(split: Collection<CharSequence>): IntArray {
        val ms = split.size
        val result = IntArray(ms)
        var i = 0
        for (buffer in split) {
            if (buffer.length == 0) {
                throw UnexpectedWhiteSpaceException()
            }
            result[i++] = findWordIndex(buffer)
        }
        return result
    }

    @Throws(WordNotFoundException::class)
    private fun findWordIndex(buffer: CharSequence): Int {
        val key = WordAndIndex(-1, buffer)
        val index = Arrays.binarySearch(words, key, wordListSortOrder)
        if (index < 0) {
            val insertionPoint = -index - 1
            var suggestion = if (insertionPoint == 0) insertionPoint else insertionPoint - 1
            if (suggestion + 1 == words.size) suggestion--
            throw WordNotFoundException(
                buffer,
                words[suggestion]!!.word,
                words[suggestion + 1]!!.word
            )
        }
        return words[index]!!.index
    }

    private inner class WordAndIndex internal constructor(i: Int, val word: CharSequence) {
        val normalized: String
        val index: Int

        init {
            normalized = normalizer.normalize(word)
            index = i
        }
    }

    companion object {
        /**
         * Get a Mnemonic validator for the given word list.
         * No normalization is currently performed, this is an open issue: https://github.com/NovaCrypto/BIP39/issues/13
         *
         * @param wordList A WordList implementation
         * @return A validator
         */
        fun ofWordList(wordList: WordList): MnemonicValidator {
            return MnemonicValidator(wordList)
        }

        @Throws(InvalidWordCountException::class, InvalidChecksumException::class)
        private fun validate(wordIndexes: IntArray) {
            val ms = wordIndexes.size
            val entPlusCs = ms * 11
            val ent = entPlusCs * 32 / 33
            val cs = ent / 32
            if (entPlusCs != ent + cs) throw InvalidWordCountException()
            val entropyWithChecksum = ByteArray((entPlusCs + 7) / 8)

            wordIndexesToEntropyWithCheckSum(wordIndexes, entropyWithChecksum)
            Arrays.fill(wordIndexes, 0);

            val entropy = entropyWithChecksum.copyOf(entropyWithChecksum.size - 1)
            val lastByte = entropyWithChecksum[entropyWithChecksum.size - 1]
            Arrays.fill(entropyWithChecksum, 0)

            val sha = MnemonicGenerator.firstByteOfSha256(entropy)

            val mask = maskOfFirstNBits(cs)

            if (((sha xor lastByte) and mask) != 0.toByte()) {
                throw  InvalidChecksumException()
            }
        }

        private fun wordIndexesToEntropyWithCheckSum(
            wordIndexes: IntArray,
            entropyWithChecksum: ByteArray
        ) {
            var i = 0
            var bi = 0
            while (i < wordIndexes.size) {
                entropyWithChecksum.writeNext11(wordIndexes[i], bi)
                i++
                bi += 11
            }
        }

        private fun maskOfFirstNBits(n: Int): Byte {
            return ((1 shl 8 - n) - 1).inv().toByte()
        }

        private val wordListSortOrder = Comparator<WordAndIndex?> { o1, o2 ->
            CharSequenceComparators.ALPHABETICAL.compare(o1?.normalized, o2?.normalized)
        }
    }

    init {
        normalizer = WordListMapNormalization(wordList)
        words = arrayOfNulls(1 shl 11)
        for (i in 0 until (1 shl 11)) {
            words[i] = WordAndIndex(i, wordList.getWord(i))
        }
        charSequenceSplitter =
            CharSequenceSplitter(wordList.getSpace(), normalizeNFKD(wordList.getSpace()))
        Arrays.sort(words, wordListSortOrder)
    }
}
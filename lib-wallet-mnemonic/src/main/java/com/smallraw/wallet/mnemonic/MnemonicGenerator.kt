package com.smallraw.wallet.mnemonic

import com.smallraw.crypto.core.crypto.Sha256.sha256
import com.smallraw.wallet.mnemonic.extensions.next11Bits
import java.util.*

/**
 * Create a generator using the given word list.
 *
 * @param wordList A known ordered list of 2048 words to select from.
 */
class MnemonicGenerator(private val wordList: WordList) {
    fun createMnemonic(
        entropyHex: CharSequence
    ): List<String> {
        val length = entropyHex.length
        if (length % 2 == 1) throw RuntimeException("Length of hex chars must be divisible by 2")
        val entropy = ByteArray(length / 2)
        return try {
            var i = 0
            var j = 0
            while (i < length) {
                entropy[j] =
                    (parseHex(entropyHex[i]) shl 4 or parseHex(
                        entropyHex[i + 1]
                    )).toByte()
                i += 2
                j++
            }
            createMnemonic(entropy)
        } finally {
            Arrays.fill(entropy, 0.toByte())
        }
    }

    fun createMnemonic(
        entropy: ByteArray
    ): List<String> {
        val wordIndexes = wordIndexes(entropy)
        return try {
            createMnemonic(wordIndexes)
        } finally {
            Arrays.fill(wordIndexes, 0)
        }
    }

    private fun createMnemonic(
        wordIndexes: IntArray
    ): List<String> {
        val mnemonicList: MutableList<String> = ArrayList()
        val space = wordList.getSpace().toString()
        for (i in wordIndexes.indices) {
            mnemonicList.add(wordList.getWord(wordIndexes[i]))
        }
        return mnemonicList
    }

    companion object {
        private fun wordIndexes(entropy: ByteArray): IntArray {
            val ent = entropy.size * 8
            entropyLengthPreChecks(ent)
            val entropyWithChecksum = Arrays.copyOf(entropy, entropy.size + 1)
            entropyWithChecksum[entropy.size] = firstByteOfSha256(entropy)

            //checksum length
            val cs = ent / 32
            //mnemonic length
            val ms = (ent + cs) / 11

            //get the indexes into the word list
            val wordIndexes = IntArray(ms)
            var i = 0
            var wi = 0
            while (wi < ms) {
                wordIndexes[wi] = entropyWithChecksum.next11Bits(i)
                i += 11
                wi++
            }
            return wordIndexes
        }

        private fun firstByteOfSha256(entropy: ByteArray?): Byte {
            val hash = sha256(entropy!!)
            val firstByte = hash[0]
            Arrays.fill(hash, 0.toByte())
            return firstByte
        }

        private fun entropyLengthPreChecks(ent: Int) {
            if (ent < 128) throw RuntimeException("Entropy too low, 128-256 bits allowed")
            if (ent > 256) throw RuntimeException("Entropy too high, 128-256 bits allowed")
            if (ent % 32 > 0) throw RuntimeException("Number of entropy bits must be divisible by 32")
        }

        private fun parseHex(c: Char): Int {
            if (c >= '0' && c <= '9') return c - '0'
            if (c >= 'a' && c <= 'f') return c - 'a' + 10
            if (c >= 'A' && c <= 'F') return c - 'A' + 10
            throw RuntimeException("Invalid hex char '$c'")
        }
    }
}
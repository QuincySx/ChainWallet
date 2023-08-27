package com.smallraw.wallet.mnemonic

import com.smallraw.crypto.core.crypto.Pbkdf2
import com.smallraw.wallet.mnemonic.Normalization.normalizeNFKD
import com.smallraw.wallet.mnemonic.extensions.toUTF8ByteArray
import java.util.Arrays

class SeedCalculator {
    private val fixedSalt = "mnemonic".toByteArray()

    fun calculateSeed(mnemonicList: List<String?>?, passphrase: String?): ByteArray {
        if (mnemonicList == null || mnemonicList.size < 3) {
            throw RuntimeException("The dictionary cannot be empty and the number of words must not be less than 3")
        }
        val stringBuilder = StringBuilder()
        for (str in mnemonicList) {
            stringBuilder.append(str).append(" ")
        }
        val mnemonic = stringBuilder.substring(0, stringBuilder.length - 1).toString()
        return calculateSeed(mnemonic, passphrase)
    }

    /**
     * Calculate the seed given a mnemonic and corresponding passphrase.
     * The phrase is not checked for validity here, for that use a [MnemonicValidator].
     *
     *
     * Due to normalization, these need to be [String], and not [CharSequence], this is an open issue:
     * https://github.com/NovaCrypto/BIP39/issues/7
     *
     *
     * If you have a list of words selected from a word list, you can use [.withWordsFromWordList] then
     * [SeedCalculatorByWordListLookUp.calculateSeed]
     *
     * @param mnemonic   The memorable list of words
     * @param passphrase An optional passphrase, use "" if not required
     * @return a seed for HD wallet generation
     */
    fun calculateSeed(mnemonic: String?, passphrase: String?): ByteArray {
        val chars = normalizeNFKD(mnemonic).toCharArray()
        return try {
            calculateSeed(chars, passphrase)
        } finally {
            Arrays.fill(chars, '\u0000')
        }
    }

    fun calculateSeed(mnemonicChars: CharArray, passphrase: String?): ByteArray {
        val normalizedPassphrase = normalizeNFKD(passphrase)
        val salt2 = normalizedPassphrase.toByteArray()
        val salt = fixedSalt.plus(salt2)
        salt2.fill(0)
        val encoded = hash(mnemonicChars, salt)
        salt.fill(0)
        return encoded
    }

    fun withWordsFromWordList(
        wordList: WordList
    ): SeedCalculatorByWordListLookUp {
        return SeedCalculatorByWordListLookUp(this, wordList)
    }

    private fun hash(chars: CharArray, salt: ByteArray): ByteArray {
        val bytes = chars.toUTF8ByteArray()

        return Pbkdf2.hmacSha512(bytes, salt)
    }
}
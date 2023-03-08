package com.smallraw.wallet.mnemonic

import android.content.Context
import com.smallraw.wallet.mnemonic.exception.InvalidChecksumException
import com.smallraw.wallet.mnemonic.exception.InvalidWordCountException
import com.smallraw.wallet.mnemonic.exception.MnemonicValidatorException
import com.smallraw.wallet.mnemonic.exception.MnemonicWordException
import com.smallraw.wallet.mnemonic.exception.UnexpectedWhiteSpaceException
import com.smallraw.wallet.mnemonic.exception.WordNotFoundException
import java.security.SecureRandom
import java.util.*

class MnemonicBuild(private val context: Context) {

    /**
     * 随机创建助记词
     * @param wordCount 助记词个数
     * @param wordType 助记词语言类型
     * @param random 随机数生成器
     */
    fun createMnemonic(
        wordCount: WordCount = WordCount.TWENTY_FOUR,
        wordType: WordType = WordType.ENGLISH,
        random: Random = SecureRandom()
    ): List<String> {
        val randomBytes = RandomSeed.random(wordCount, random)
        return MnemonicGenerator(WordList(context, wordType)).createMnemonic(randomBytes)
    }

    /**
     * 随机创建种子
     * @param wordCount 助记词个数
     * @param wordType 助记词语言类型
     * @param random 随机数生成器
     * @param passphrase 助记词的盐
     */
    fun createSeed(
        wordCount: WordCount = WordCount.TWENTY_FOUR,
        wordType: WordType = WordType.ENGLISH,
        random: Random = SecureRandom(),
        passphrase: String = ""
    ): ByteArray {
        val mnemonic = createMnemonic(wordCount, wordType, random)
        return SeedCalculator()
            .withWordsFromWordList(WordList(context, wordType))
            .calculateSeed(mnemonic, passphrase)
    }

    /**
     * 根据助记词创建种子
     * @param charSequence 助记词,空格间隔开。
     * @param passphrase 助记词的盐
     */
    fun createSeedByMnemonic(
        charSequence: CharSequence,
        passphrase: String = ""
    ): ByteArray {
        val mnemonic = CharSequenceSplitter.split(charSequence)
        return createSeedByMnemonic(mnemonic, passphrase)
    }

    /**
     * 根据助记词创建种子
     * @param charSequence 助记词
     * @param passphrase 助记词的盐
     */
    fun createSeedByMnemonic(
        mnemonic: Collection<CharSequence>,
        passphrase: String = ""
    ): ByteArray {
        val seedCalculator = SeedCalculator()
        return seedCalculator
            .withWordsFromWordList(WordList(context, WordType.ENGLISH))
            .calculateSeed(mnemonic, passphrase)
    }

    /**
     * 根据助记词创建种子,可以支持只输入单词前四个字母即可创建种子。
     * @param wordType 输入助记词的语言类型
     * @param charSequence 助记词,空格间隔开。
     * @param passphrase 助记词的盐
     * @exception MnemonicWordException 助记词不正确
     */
    @Throws(
        MnemonicWordException::class
    )
    fun createSeedByMnemonic(
        wordType: WordType,
        charSequence: CharSequence,
        passphrase: String = ""
    ): ByteArray {
        val mnemonic = CharSequenceSplitter.split(charSequence)
        return createSeedByMnemonic(wordType, mnemonic, passphrase)
    }

    /**
     * 根据助记词创建种子
     * @param wordType 输入助记词的语言类型
     * @param charSequence 助记词,可以支持只输入单词前四个字母即可创建种子。
     * @param passphrase 助记词的盐
     * @exception MnemonicWordException 助记词不正确
     */
    @Throws(
        MnemonicWordException::class
    )
    fun createSeedByMnemonic(
        wordType: WordType,
        mnemonic: Collection<CharSequence>,
        passphrase: String = ""
    ): ByteArray {
        val seedCalculator = SeedCalculator()
        return seedCalculator
            .withWordsFromWordList(WordList(context, wordType, true))
            .calculateSeed(mnemonic, passphrase)
    }

    /**
     * 验证助记词是否正确
     * @param charSequence 助记词,空格间隔开。
     * @param passphrase 助记词的盐
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
    fun validateMnemonic(
        mnemonic: CharSequence,
        wordType: WordType
    ) {
        MnemonicValidator.ofWordList(WordList(context, wordType))
            .validate(mnemonic)
    }

    /**
     * 验证助记词是否正确
     * @param charSequence 助记词
     * @param passphrase 助记词的盐
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
    fun validateMnemonic(
        mnemonic: Collection<CharSequence>,
        wordType: WordType
    ) {
        MnemonicValidator.ofWordList(WordList(context, wordType))
            .validate(mnemonic)
    }

    /**
     * 验证助记词是否正确
     * @param charSequence 助记词
     * @param passphrase 助记词的盐
     * @return true 助记词正确，false 助记词不正确
     */
    fun validateMnemonic(
        charSequence: CharSequence
    ): Boolean {
        val mnemonic = CharSequenceSplitter.split(charSequence)
        return validateMnemonic(mnemonic)
    }

    /**
     * 验证助记词是否正确
     * @param charSequence 助记词
     * @param passphrase 助记词的盐
     * @return true 助记词正确，false 助记词不正确
     */
    fun validateMnemonic(
        mnemonic: Collection<CharSequence>
    ): Boolean {
        listOf(
            WordType.ENGLISH,
            WordType.CHINESE_SIMPLIFIED,
            WordType.CHINESE_TRADITIONAL,
            WordType.JAPANESE,
            WordType.ITALIAN,
            WordType.KOREAN,
            WordType.CZECH,
            WordType.FRENCH,
            WordType.SPANISH
        ).forEach { wordType ->
            try {
                MnemonicValidator.ofWordList(WordList(context, wordType))
                    .validate(mnemonic)
                return true
            } catch (e: MnemonicValidatorException) {
                e.printStackTrace()
            }
        }
        return false
    }
}
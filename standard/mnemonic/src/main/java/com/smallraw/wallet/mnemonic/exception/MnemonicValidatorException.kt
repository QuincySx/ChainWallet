package com.smallraw.wallet.mnemonic.exception

open class MnemonicValidatorException @JvmOverloads constructor(msg: String? = null) :
    Exception(msg ?: "") {

}

class InvalidChecksumException : MnemonicValidatorException("Invalid checksum")
class InvalidWordCountException : MnemonicValidatorException("Not a correct number of words")
class UnexpectedWhiteSpaceException : MnemonicValidatorException("Unexpected whitespace")
class WordNotFoundException(
    val word: CharSequence,
    val suggestion1: CharSequence,
    val suggestion2: CharSequence
) : MnemonicValidatorException(
    String.format(
        "Word not found in word list \"%s\", suggestions \"%s\", \"%s\"",
        word,
        suggestion1,
        suggestion2
    )
)

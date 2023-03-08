package com.smallraw.wallet.mnemonic.exception

open class MnemonicException @JvmOverloads constructor(msg: String? = null) :
    Exception(msg ?: "") {

}
class MnemonicWordException(
    /**
     * Contains the word that was not found in the word list.
     */
    val badWord: String
) : MnemonicException()
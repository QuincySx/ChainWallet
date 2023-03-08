package com.smallraw.wallet.mnemonic.jni

class Pbkdf2JNI {
    companion object {
        init {
            System.loadLibrary("pbkdf2-wrapper")
        }
    }

    external fun pbkdf2HmacSha256(
        mnemonicChars: CharArray,
        passphrase: ByteArray,
        mnemonicSize: Int = mnemonicChars.size,
        passphraseSize: Int = passphrase.size,
        iterations:Int = 2048
    ): ByteArray?

    external fun pbkdf2HmacSha512(
        mnemonicChars: ByteArray,
        passphrase: ByteArray,
        mnemonicSize: Int = mnemonicChars.size,
        passphraseSize: Int = passphrase.size,
        iterations:Int = 2048
    ): ByteArray?
}
package com.smallraw.chain.lib

abstract class ChainAccount(
    private val mKeyPair: BaseKeyPair
) {
    abstract fun createAddress(): Address
    abstract fun createSigner(): Signer

    fun getPublicKey() = mKeyPair.getPublicKey()

    fun getPrivateKey() = mKeyPair.getPrivateKey()

    fun getAddress() = createAddress()

    fun sign(message: ByteArray): Signature {
        return createSigner().sign(mKeyPair.getPrivateKey(), message)
    }
}
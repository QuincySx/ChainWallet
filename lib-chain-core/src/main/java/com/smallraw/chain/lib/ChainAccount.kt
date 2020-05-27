package com.smallraw.chain.lib

import java.security.PublicKey

abstract class ChainAccount(
    private val mKeyPair: BaseKeyPair
) {
    abstract fun createAddress(mPublicKey: PublicKey): Address
    abstract fun createSigner(): Signer

    fun getPublicKey() = mKeyPair.getPublicKey()

    fun getPrivateKey() = mKeyPair.getPrivateKey()

    fun getAddress() = createAddress(getPublicKey())

    fun sign(message: ByteArray): Signature {
        return createSigner().sign(mKeyPair.getPrivateKey(), message)
    }
}
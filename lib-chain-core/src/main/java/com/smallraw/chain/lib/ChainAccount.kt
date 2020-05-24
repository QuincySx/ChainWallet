package com.smallraw.chain.lib

import java.lang.IllegalArgumentException
import java.security.PrivateKey
import java.security.PublicKey

abstract class ChainAccount(
    protected val mPrivateKey: PrivateKey? = null,
    @Volatile
    protected var mPublicKey: PublicKey? = null
) {
    abstract fun createPublicGenerator(): PublicGenerator
    abstract fun createAddress(mPublicKey: PublicKey): Address
    abstract fun createSigner(): Signer

    fun getPublicKey(): PublicKey {
        if (null == mPrivateKey && null != mPublicKey) {
            return mPublicKey as PublicKey
        }
        if (null == mPrivateKey) {
            throw IllegalArgumentException("mPrivateKey is null")
        }
        return mPublicKey ?: synchronized(this) {
            mPublicKey ?: createPublicGenerator().generate(mPrivateKey).public.also {
                mPublicKey = it
            }
        }
    }

    fun getAddress() = createAddress(getPublicKey())

    fun sign(message: ByteArray): Signature {
        if (null == mPrivateKey) {
            throw IllegalArgumentException("mPrivateKey is null")
        }
        return createSigner().sign(mPrivateKey, message)
    }
}
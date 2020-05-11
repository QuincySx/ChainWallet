package com.smallraw.chain.lib

import java.lang.IllegalArgumentException
import java.security.PrivateKey
import java.security.PublicKey

abstract class ChainAccount(
    protected val mPublicGenerator: PublicGenerator,
    protected val mAddress: Address,
    protected val mSigner: Signer,
    protected val mPrivateKey: PrivateKey? = null,
    @Volatile
    protected var mPublicKey: PublicKey? = null
) {

    fun getPublicKey(): PublicKey {
        if (null == mPrivateKey && null != mPublicKey) {
            return mPublicKey as PublicKey
        }
        if (null == mPrivateKey) {
            throw IllegalArgumentException("mPrivateKey is null")
        }
        return mPublicKey ?: synchronized(this) {
            mPublicKey ?: mPublicGenerator.generate(mPrivateKey).public.also {
                mPublicKey = it
            }
        }
    }

    fun getAddress() = mAddress

    fun sign(message: ByteArray): Signature {
        if (null == mPrivateKey) {
            throw IllegalArgumentException("mPrivateKey is null")
        }
        return mSigner.sign(mPrivateKey, message)
    }
}
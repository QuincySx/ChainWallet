package com.smallraw.crypto.core

import java.security.PrivateKey
import java.security.PublicKey

abstract class BaseKeyPair(
    private var privateKey: PrivateKey? = null,
    private var publicKey: PublicKey? = null
) {
    fun getPrivateKey(): PrivateKey {
        return privateKey ?: synchronized(this) {
            privateKey ?: generatorPrivateKey().also {
                privateKey = it
            }
        }
    }

    fun getPublicKey(): PublicKey {
        return publicKey ?: synchronized(this) {
            publicKey ?: generatorPublicKey().also {
                publicKey = it
            }
        }
    }

    protected abstract fun generatorPrivateKey(): PrivateKey
    protected abstract fun generatorPublicKey(): PublicKey
}
package com.smallraw.chain.eos

import com.smallraw.crypto.Secp256k1KeyPair
import com.smallraw.crypto.Secp256k1PrivateKey
import com.smallraw.crypto.Secp256k1PublicKey

class KeyPair(
    privateKey: PrivateKey? = null,
    publicKey: PublicKey? = null
) {
    companion object {
        fun ofSecretKey(secretKey: ByteArray): KeyPair {
            return KeyPair(PrivateKey(secretKey))
        }

        fun ofSecretKeyHex(secretKeyHex: String): KeyPair {
            return KeyPair(PrivateKey.ofHex(secretKeyHex))
        }
    }

    private val mSecp256k1KeyPair by lazy {
        Secp256k1KeyPair(
            privateKey?.getKey()?.let { Secp256k1PrivateKey(it) },
            publicKey?.getKey()?.let { Secp256k1PublicKey(it) },
            false
        )
    }

    fun getPrivateKey(): PrivateKey {
        return PrivateKey(mSecp256k1KeyPair.getPrivateKey().encoded)
    }

    fun getPublicKey(): PublicKey {
        return PublicKey(mSecp256k1KeyPair.getPublicKey().encoded)
    }
}
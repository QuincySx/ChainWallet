package com.smallraw.chain.lib

import com.smallraw.chain.lib.crypto.Secp256K1
import com.smallraw.chain.lib.execptions.JNICallException
import java.security.PrivateKey
import java.security.PublicKey

class Secp256k1KeyPair(
    privateKey: PrivateKey?,
    publicKey: PublicKey?,
    val compressed: Boolean
) : BaseKeyPair(privateKey, publicKey) {
    override fun generatorPrivateKey(): PrivateKey {
        return Secp256k1PrivateKey(Secp256K1.createPrivateKey())
    }

    override fun generatorPublicKey(): PublicKey {
        val bytes = Secp256K1.createPublicKey(getPrivateKey().encoded, compressed)
        return Secp256k1PublicKey(bytes)
    }
}
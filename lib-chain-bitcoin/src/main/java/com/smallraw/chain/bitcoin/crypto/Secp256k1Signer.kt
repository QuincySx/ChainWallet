package com.smallraw.chain.bitcoin.crypto

import com.smallraw.chain.lib.core.Signature
import com.smallraw.chain.lib.core.Signer
import com.smallraw.chain.lib.crypto.Secp256K1
import java.security.PrivateKey

class Secp256k1Signer : Signer {
    override fun sign(privateKey: PrivateKey, message: ByteArray): Signature {
        return Signature(Secp256K1.sign(privateKey.encoded, message))
    }

    override fun verify(publicKey: ByteArray, signature: ByteArray, message: ByteArray): Boolean {
        return Secp256K1.verify(publicKey, signature, message)
    }
}
package com.smallraw.chain.lib.crypto

import java.security.PrivateKey

class Secp256k1Signer : Signer {
    override fun sign(privateKey: PrivateKey, message: ByteArray): Signature {
        return object : Signature {}
    }
}
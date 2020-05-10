package com.smallraw.chain.lib.crypto.impl

import com.smallraw.chain.lib.crypto.Signature
import com.smallraw.chain.lib.crypto.Signer
import java.security.PrivateKey

class Secp256k1Signer : Signer {
    override fun sign(privateKey: PrivateKey, message: ByteArray): Signature {
        return object : Signature {}
    }
}
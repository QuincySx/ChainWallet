package com.smallraw.chain.lib

import com.smallraw.chain.lib.core.extensions.toHex
import java.security.PublicKey

class Secp256k1PublicKey(private val publicKey: ByteArray) : PublicKey {
    override fun getAlgorithm(): String {
        return "secp256k1"
    }

    override fun getEncoded(): ByteArray {
        return publicKey
    }

    override fun getFormat(): String {
        return encoded.toHex()
    }
}
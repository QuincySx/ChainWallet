package com.smallraw.chain.lib

import com.smallraw.chain.lib.execptions.PrivateKeyException
import com.smallraw.chain.lib.extensions.toHex
import java.security.PrivateKey

class Secp256k1PrivateKey
@Throws(PrivateKeyException.AbnormalLength::class)
constructor(private val secretKey: ByteArray) :
    PrivateKey {

    init {
        if (secretKey.size != 32) throw PrivateKeyException.AbnormalLength()
    }

    override fun getAlgorithm(): String {
        return "secp256k1"
    }

    override fun getEncoded(): ByteArray {
        return secretKey
    }

    override fun getFormat(): String {
        return encoded.toHex()
    }
}
package com.smallraw.chain.lib.crypto

import com.smallraw.chain.lib.Secp256k1PublicKey
import com.smallraw.chain.lib.PublicGenerator
import com.smallraw.chain.lib.execptions.PrivateKeyException
import com.smallraw.chain.lib.execptions.PublicKeyException
import java.security.KeyPair
import java.security.PrivateKey

class BitcoinPublicGenerator(private val compressed: Boolean) : PublicGenerator {
    @Throws(PrivateKeyException.AbnormalLength::class, PublicKeyException.CreateException::class)
    override fun generate(privateKey: PrivateKey): KeyPair {
        val createPublicKey =
            Secp256K1.createPublicKey(privateKey.encoded, compressed)
                ?: throw PublicKeyException.CreateException()
        return KeyPair(
            Secp256k1PublicKey(
                createPublicKey
            ), privateKey)
    }
}
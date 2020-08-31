package com.smallraw.chain.lib

import com.smallraw.chain.lib.bitcoin.BitcoinAddress
import com.smallraw.chain.lib.bitcoin.convert.WalletImportFormat
import com.smallraw.chain.lib.bitcoin.transaction.build.IPrivateKeyPairProvider
import com.smallraw.chain.lib.extensions.hexStringToByteArray
import java.security.PublicKey

class UnitTestIPrivateKeyPairProvider(private val wifPrivate: String) :
    IPrivateKeyPairProvider {
    override fun findByPrivate(publicKey: PublicKey): Secp256k1KeyPair {
        return Secp256k1KeyPair(
            Secp256k1PrivateKey(wifPrivate.hexStringToByteArray()),
            null,
            true
        )
    }

    override fun findByAddress(address: BitcoinAddress): Secp256k1KeyPair {
        val decode =
            WalletImportFormat.decode(wifPrivate)
        return Secp256k1KeyPair(Secp256k1PrivateKey(decode.privateKey), null, true)
    }
}
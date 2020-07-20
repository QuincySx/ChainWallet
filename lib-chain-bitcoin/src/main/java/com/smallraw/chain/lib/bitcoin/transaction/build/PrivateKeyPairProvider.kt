package com.smallraw.chain.lib.bitcoin.transaction.build

import com.smallraw.chain.lib.Secp256k1KeyPair
import com.smallraw.chain.lib.Secp256k1PrivateKey
import com.smallraw.chain.lib.bitcoin.BitcoinAddress
import com.smallraw.chain.lib.bitcoin.convert.WalletImportFormat
import com.smallraw.chain.lib.extensions.hexStringToByteArray
import java.security.PrivateKey
import java.security.PublicKey

class PrivateKeyPairProvider() {
    fun findByPrivate(publicKey: PublicKey): Secp256k1KeyPair {
        return Secp256k1KeyPair(
            Secp256k1PrivateKey("cRvyLwCPLU88jsyj94L7iJjQX5C2f8koG4G2gevN4BeSGcEvfKe9".hexStringToByteArray()),
            null,
            true
        )
    }

    fun findByAddress(address: BitcoinAddress): Secp256k1KeyPair {
        val decode =
            WalletImportFormat.decode("cRvyLwCPLU88jsyj94L7iJjQX5C2f8koG4G2gevN4BeSGcEvfKe9")
        return Secp256k1KeyPair(Secp256k1PrivateKey(decode.privateKey), null, true)
    }
}

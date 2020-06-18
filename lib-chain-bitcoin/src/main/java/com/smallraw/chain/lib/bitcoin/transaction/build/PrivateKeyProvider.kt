package com.smallraw.chain.lib.bitcoin.transaction.build

import com.smallraw.chain.lib.Secp256k1PrivateKey
import com.smallraw.chain.lib.bitcoin.BitcoinAccount
import com.smallraw.chain.lib.bitcoin.BitcoinAddress
import com.smallraw.chain.lib.bitcoin.format.WalletImportFormat
import com.smallraw.chain.lib.extensions.hexStringToByteArray
import java.security.PrivateKey
import java.security.PublicKey

class PrivateKeyProvider {
    fun findByPrivate(publicKey: PublicKey): PrivateKey {
        return Secp256k1PrivateKey("cRvyLwCPLU88jsyj94L7iJjQX5C2f8koG4G2gevN4BeSGcEvfKe9".hexStringToByteArray())
    }

    fun findByAddress(address: BitcoinAddress): PrivateKey {
        val decode =
            WalletImportFormat.decode("cRvyLwCPLU88jsyj94L7iJjQX5C2f8koG4G2gevN4BeSGcEvfKe9")
        return Secp256k1PrivateKey(decode.privateKey)
    }
}

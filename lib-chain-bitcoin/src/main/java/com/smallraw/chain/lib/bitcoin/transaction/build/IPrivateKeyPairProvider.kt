package com.smallraw.chain.lib.bitcoin.transaction.build

import com.smallraw.chain.lib.Secp256k1KeyPair
import com.smallraw.chain.lib.Secp256k1PrivateKey
import com.smallraw.chain.lib.bitcoin.BitcoinAddress
import com.smallraw.chain.lib.bitcoin.convert.WalletImportFormat
import com.smallraw.chain.lib.extensions.hexStringToByteArray
import java.security.PublicKey

interface IPrivateKeyPairProvider {
    fun findByPrivate(publicKey: PublicKey): Secp256k1KeyPair
    fun findByAddress(address: BitcoinAddress): Secp256k1KeyPair
}

package com.smallraw.chain.lib.bitcoin.transaction.build

import com.smallraw.chain.lib.Secp256k1KeyPair
import com.smallraw.chain.lib.bitcoin.Bitcoin
import java.security.PublicKey

interface IPrivateKeyPairProvider {
    fun findByPrivate(publicKey: PublicKey): Bitcoin.KeyPair
    fun findByAddress(address: Bitcoin.Address): Bitcoin.KeyPair
}

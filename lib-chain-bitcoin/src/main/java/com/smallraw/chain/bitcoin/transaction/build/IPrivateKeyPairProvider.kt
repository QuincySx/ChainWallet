package com.smallraw.chain.bitcoin.transaction.build

import com.smallraw.chain.bitcoin.Bitcoin
import java.security.PublicKey

interface IPrivateKeyPairProvider {
    fun findByPrivate(publicKey: PublicKey): Bitcoin.KeyPair
    fun findByAddress(address: Bitcoin.Address): Bitcoin.KeyPair
}

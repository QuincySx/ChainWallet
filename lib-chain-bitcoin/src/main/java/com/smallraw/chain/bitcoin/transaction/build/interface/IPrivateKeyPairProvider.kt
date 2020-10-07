package com.smallraw.chain.bitcoin.transaction.build.`interface`

import com.smallraw.chain.bitcoin.Bitcoin

interface IPrivateKeyPairProvider {
    fun findByPrivate(publicKey: Bitcoin.PublicKey): Bitcoin.KeyPair
    fun findByAddress(address: Bitcoin.Address): Bitcoin.KeyPair
}

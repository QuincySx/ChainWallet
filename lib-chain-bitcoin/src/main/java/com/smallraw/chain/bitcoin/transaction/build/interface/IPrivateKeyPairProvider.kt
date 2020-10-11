package com.smallraw.chain.bitcoin.transaction.build.`interface`

import com.smallraw.chain.bitcoin.Bitcoin
import com.smallraw.chain.bitcoincore.PublicKey
import com.smallraw.chain.bitcoincore.address.Address

interface IPrivateKeyPairProvider {
    fun findByPrivate(publicKey: PublicKey): Bitcoin.KeyPair
    fun findByAddress(address: Address): Bitcoin.KeyPair
}

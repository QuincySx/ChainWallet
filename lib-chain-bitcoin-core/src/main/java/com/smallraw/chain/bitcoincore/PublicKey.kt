package com.smallraw.chain.bitcoincore

import com.smallraw.chain.bitcoincore.address.P2PKHAddress
import com.smallraw.chain.bitcoincore.address.P2WPKHAddress
import com.smallraw.chain.bitcoincore.network.BaseNetwork
import com.smallraw.chain.lib.crypto.Ripemd160

class PublicKey(private val key: ByteArray) {
    fun getHash() = Ripemd160.hash160(key)

    fun getKey() = key

    fun getAddress(network: BaseNetwork, isSegwit: Boolean = false) = if (isSegwit) {
        P2WPKHAddress(network, hashKey = getHash())
    } else {
        P2PKHAddress(network, hashKey = getHash())
    }
}
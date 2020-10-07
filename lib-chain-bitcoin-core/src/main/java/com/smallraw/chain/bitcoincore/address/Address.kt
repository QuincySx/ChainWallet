package com.smallraw.chain.bitcoincore.address

import com.smallraw.chain.bitcoincore.network.BaseNetwork
import com.smallraw.chain.bitcoincore.script.Script

abstract class Address(protected val network: BaseNetwork) {
    enum class AddressType {
        P2PKH,  // Pay to public key hash
        P2SH,   // Pay to script hash
        P2WSHV0, // Pay to witness hash
        P2WPKHV0 // Pay to witness hash
    }

    abstract fun toHash(): ByteArray

    abstract override fun toString(): String

    abstract fun getType(): AddressType

    abstract fun lockScript(): Script
}
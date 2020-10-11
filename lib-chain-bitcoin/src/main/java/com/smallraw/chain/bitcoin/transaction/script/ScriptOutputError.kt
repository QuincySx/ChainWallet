package com.smallraw.chain.bitcoin.transaction.script

import com.smallraw.chain.bitcoincore.address.Address
import com.smallraw.chain.bitcoincore.network.BaseNetwork

class ScriptOutputError(scriptBytes: ByteArray) : ScriptOutput(scriptBytes) {
    override fun getAddress(network: BaseNetwork): Address? {
        return null
    }

    override fun getAddressBytes() = null
}
package com.smallraw.chain.bitcoin.transaction.script

import com.smallraw.chain.bitcoin.Bitcoin
import com.smallraw.chain.bitcoin.network.BaseNetwork

class ScriptOutputError(scriptBytes: ByteArray) : ScriptOutput(scriptBytes) {
    override fun getAddress(network: BaseNetwork): Bitcoin.Address {
        return Bitcoin.Address.getNullAddress(network)
    }

    override fun getAddressBytes() = byteArrayOf()
}
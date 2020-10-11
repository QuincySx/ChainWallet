package com.smallraw.chain.bitcoin.transaction.script

import com.smallraw.chain.bitcoincore.address.Address
import com.smallraw.chain.bitcoincore.network.BaseNetwork
import com.smallraw.chain.bitcoincore.script.ScriptChunk

/**
 * 不知道的交易脚本
 */
class ScriptOutputStrange : ScriptOutput {
    constructor(chunks: List<ScriptChunk>, scriptBytes: ByteArray) : super(scriptBytes)

    override fun getAddress(network: BaseNetwork): Address? {
        // We cannot determine the address from scripts we do not understand
        return null
    }

    override fun getAddressBytes(): ByteArray {
        return byteArrayOf()
    }
}
package com.smallraw.chain.bitcoin.transaction.script

import com.smallraw.chain.bitcoin.Bitcoin
import com.smallraw.chain.bitcoin.network.BaseNetwork

/**
 * 不知道的交易脚本
 */
class ScriptOutputStrange : ScriptOutput {
    constructor(chunks: List<Chunk>, scriptBytes: ByteArray) : super(scriptBytes)

    override fun getAddress(network: BaseNetwork): Bitcoin.Address {
        // We cannot determine the address from scripts we do not understand
        return Bitcoin.Address.getNullAddress()
    }

    override fun getAddressBytes(): ByteArray {
        return byteArrayOf()
    }
}
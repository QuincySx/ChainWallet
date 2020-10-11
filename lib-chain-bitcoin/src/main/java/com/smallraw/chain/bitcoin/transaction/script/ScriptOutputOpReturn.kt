package com.smallraw.chain.bitcoin.transaction.script

import com.smallraw.chain.bitcoincore.address.Address
import com.smallraw.chain.bitcoincore.network.BaseNetwork
import com.smallraw.chain.bitcoincore.script.OP_RETURN
import com.smallraw.chain.bitcoincore.script.ScriptChunk
import com.smallraw.chain.bitcoincore.script.isOP

class ScriptOutputOpReturn : ScriptOutput {
    companion object {
        fun isScriptOutputOpReturn(chunks: List<ScriptChunk>): Boolean {
            // {{OP_RETURN},{something non-null non-empty}}
            return chunks.size == 2 &&
                    chunks[0].isOP(OP_RETURN) &&
                    chunks[1] != null &&
                    chunks[1].toBytes().isNotEmpty()
        }
    }

    private val dataBytes: ByteArray

    constructor(chunks: List<ScriptChunk>, scriptBytes: ByteArray) : super(scriptBytes) {
        dataBytes = chunks[1].toBytes()
    }

    /**
     * Get the data bytes contained in this output.
     *
     * @return The data bytes of this output.
     */
    fun getDataBytes(): ByteArray? {
        return dataBytes
    }

    override fun getAddress(network: BaseNetwork): Address? {
        return null
    }

    override fun getAddressBytes() = null
}
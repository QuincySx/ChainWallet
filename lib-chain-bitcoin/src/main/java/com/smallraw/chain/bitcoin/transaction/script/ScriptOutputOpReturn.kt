package com.smallraw.chain.bitcoin.transaction.script

import com.smallraw.chain.bitcoin.Bitcoin
import com.smallraw.chain.bitcoin.network.BaseNetwork

class ScriptOutputOpReturn : ScriptOutput {
    companion object {
        fun isScriptOutputOpReturn(chunks: List<Chunk>): Boolean {
            // {{OP_RETURN},{something non-null non-empty}}
            return chunks.size == 2 &&
                    isOP(chunks[0], OP_RETURN) &&
                    chunks[1] != null &&
                    chunks[1].toBytes().size > 0
        }
    }

    private val dataBytes: ByteArray

    constructor(chunks: List<Chunk>, scriptBytes: ByteArray) : super(scriptBytes) {
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

    override fun getAddress(network: BaseNetwork): Bitcoin.Address {
        return Bitcoin.Address.getNullAddress(network)
    }

    override fun getAddressBytes() = byteArrayOf()
}
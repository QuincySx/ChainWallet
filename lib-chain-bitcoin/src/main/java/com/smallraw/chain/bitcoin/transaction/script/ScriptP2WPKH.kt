package com.smallraw.chain.bitcoin.transaction.script

import com.smallraw.chain.bitcoin.Bitcoin
import com.smallraw.chain.bitcoin.network.BaseNetwork

class ScriptInputP2WPKH(scriptBytes: ByteArray) : ScriptInput(scriptBytes)

/**
 * Native SegWit pay-to-witness-public-key-hash script output
 */
class ScriptOutputP2WPKH : ScriptOutput {
    companion object {
        @JvmStatic
        fun isScriptOutputP2WPKH(chunks: Array<ByteArray>): Boolean {
            if (chunks.isEmpty()) {
                return false
            }
            if (!Script.isOP(chunks[0], OP_FALSE)) {
                return false
            }
            return chunks[1].size == 20
        }

        @JvmStatic
        fun isScriptOutputP2WPKH(chunks: List<Chunk>): Boolean {
            if (chunks.isEmpty()) {
                return false
            }
            if (!Script.isOP(chunks[0].toBytes(), OP_FALSE)) {
                return false
            }
            return chunks[1].toBytes().size == 20
        }
    }

    private val addressBytes: ByteArray

    constructor(scriptBytes: ByteArray) : super(scriptBytes) {
        addressBytes = scriptBytes.copyOfRange(2, scriptBytes.size)
    }

    constructor(chunks: List<Chunk>, scriptBytes: ByteArray) : super(scriptBytes) {
        addressBytes = chunks[1].toBytes()
    }

    override fun getAddress(network: BaseNetwork): Bitcoin.Address {
        TODO("Not yet implemented")
        //SegwitAddress(network, 0x00, addressBytes)
    }

    override fun getAddressBytes() = addressBytes
}
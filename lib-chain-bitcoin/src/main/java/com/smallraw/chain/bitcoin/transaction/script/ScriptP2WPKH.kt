package com.smallraw.chain.bitcoin.transaction.script

import com.smallraw.chain.bitcoincore.address.Address
import com.smallraw.chain.bitcoincore.address.P2WPKHAddress
import com.smallraw.chain.bitcoincore.network.BaseNetwork
import com.smallraw.chain.bitcoincore.script.OP_FALSE
import com.smallraw.chain.bitcoincore.script.ScriptChunk
import com.smallraw.chain.bitcoincore.script.isOP

class ScriptInputP2WPKH(scriptBytes: ByteArray) : ScriptInput(scriptBytes)

/**
 * Native SegWit pay-to-witness-public-key-hash script output
 */
class ScriptOutputP2WPKH : ScriptOutput {
    companion object {
        @JvmStatic
        fun isScriptOutputP2WPKH(chunks: List<ScriptChunk>): Boolean {
            if (chunks.isEmpty()) {
                return false
            }
            if (!chunks[0].isOP(OP_FALSE)) {
                return false
            }
            return chunks[1].toBytes().size == 20
        }
    }

    private val addressBytes: ByteArray

    constructor(scriptBytes: ByteArray) : super(scriptBytes) {
        addressBytes = scriptBytes.copyOfRange(2, scriptBytes.size)
    }

    constructor(chunks: List<ScriptChunk>, scriptBytes: ByteArray) : super(scriptBytes) {
        addressBytes = chunks[1].toBytes()
    }

    override fun getAddress(network: BaseNetwork): Address {
        return P2WPKHAddress(
            addressBytes,
            network.addressSegwitHrp,
            null
        )
    }

    override fun getAddressBytes() = addressBytes
}
package com.smallraw.chain.bitcoin.transaction.script

import com.smallraw.chain.bitcoincore.address.Address
import com.smallraw.chain.bitcoincore.execptions.ScriptParsingException
import com.smallraw.chain.bitcoincore.network.BaseNetwork
import com.smallraw.chain.bitcoincore.script.Script
import com.smallraw.chain.bitcoincore.script.ScriptChunk

abstract class ScriptOutput : Script {
    companion object {
        fun fromScriptBytes(scriptBytes: ByteArray): ScriptOutput? {
            val chunks: List<ScriptChunk>?
            try {
                chunks = parseChunks(scriptBytes)
            } catch (e: ScriptParsingException) {
                return ScriptOutputError(scriptBytes)
            }
            if (chunks.isEmpty()) {
                return null
            }
            if (ScriptOutputP2WPKH.isScriptOutputP2WPKH(chunks)) {
                return ScriptOutputP2WPKH(chunks, scriptBytes)
            }
            if (ScriptOutputP2SH.isScriptOutputP2SH(chunks)) {
                return ScriptOutputP2SH(chunks, scriptBytes)
            }
            if (ScriptOutputP2PKH.isScriptOutputP2PKH(chunks)) {
                return ScriptOutputP2PKH(chunks, scriptBytes)
            }
            if (ScriptOutputOpReturn.isScriptOutputOpReturn(chunks)) {
                return ScriptOutputOpReturn(chunks, scriptBytes)
            }
            if (ScriptOutputP2PK.isScriptOutputP2PK(chunks)) {
                return ScriptOutputP2PK(chunks, scriptBytes)
            }
            return if (ScriptOutputMsg.isScriptOutputMsg(chunks)) {
                ScriptOutputMsg(chunks, scriptBytes)
            } else ScriptOutputStrange(chunks, scriptBytes)
        }
    }

    constructor(scriptBytes: ByteArray) : super(scriptBytes)
    constructor(chunks: List<ScriptChunk>) : super(chunks)

    abstract fun getAddress(network: BaseNetwork): Address?
    abstract fun getAddressBytes(): ByteArray?
}
package com.smallraw.chain.bitcoin.transaction.script

import com.smallraw.chain.bitcoin.Bitcoin
import com.smallraw.chain.bitcoin.network.BaseNetwork

abstract class ScriptOutput(scriptBytes: ByteArray) : Script(scriptBytes) {
    companion object {
        fun fromScriptBytes(scriptBytes: ByteArray): ScriptOutput? {
            val chunks: List<Chunk>?
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
            if (ScriptOutputPubkey.isScriptOutputPubkey(chunks)) {
                return ScriptOutputPubkey(chunks, scriptBytes)
            }
            return if (ScriptOutputMsg.isScriptOutputMsg(chunks)) {
                ScriptOutputMsg(chunks, scriptBytes)
            } else ScriptOutputStrange(chunks, scriptBytes)
        }
    }

    abstract fun getAddress(network: BaseNetwork): Bitcoin.Address
    abstract fun getAddressBytes(): ByteArray
}
package com.smallraw.chain.bitcoin.transaction.serializer

import com.smallraw.chain.bitcoin.transaction.build.InputToSign
import com.smallraw.chain.bitcoincore.script.ScriptType
import com.smallraw.chain.bitcoincore.stream.BitcoinOutputStream
import com.smallraw.chain.lib.core.extensions.hexToByteArray

object InputSerializer {
    fun serializeForSignature(input: InputToSign, forCurrentInputSignature: Boolean): ByteArray {
        val buffer = BitcoinOutputStream()

        buffer.writeBytes(input.txId.hexToByteArray().reversedArray())
        buffer.writeInt32(input.index)

        if (forCurrentInputSignature) {
            val script = when (input.scriptPubKeyType) {
                ScriptType.P2SH -> {
                    input.redeemScript?.scriptBytes ?: throw Exception("no previous output script")
                }
                else -> input.address.scriptPubKey().scriptBytes
            }
            buffer.writeVarInt(script.size.toLong())
            buffer.writeBytes(script)
        } else {
            buffer.writeVarInt(0L)
        }
        buffer.writeInt32(input.sequence)
        return buffer.toByteArray()
    }
}
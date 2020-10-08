package com.smallraw.chain.bitcoin.transaction.serializers

import com.smallraw.chain.bitcoin.stream.BitcoinOutputStream
import com.smallraw.chain.bitcoin.transaction.build.InputToSign
import com.smallraw.chain.bitcoin.transaction.script.ScriptType
import com.smallraw.chain.lib.core.extensions.hexToByteArray

object InputSerializer {
    fun serializeForSignature(input: InputToSign, forCurrentInputSignature: Boolean): ByteArray {
        val buffer = BitcoinOutputStream()

        buffer.write(input.txId.hexToByteArray().reversedArray())
        buffer.writeInt32(input.index)

        if (forCurrentInputSignature) {
            val script = when (input.address.scriptType) {
                ScriptType.P2SH -> {
                    input.redeemScript?.scriptBytes ?: throw Exception("no previous output script")
                }
                else -> input.address.lockingScript
            }
            buffer.writeVarInt(script.size.toLong())
            buffer.write(script)
        } else {
            buffer.writeVarInt(0L)
        }
        buffer.writeInt32(input.sequence)
        return buffer.toByteArray()
    }
}
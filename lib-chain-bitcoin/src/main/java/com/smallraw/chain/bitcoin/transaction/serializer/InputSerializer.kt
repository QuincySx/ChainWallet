package com.smallraw.chain.bitcoin.transaction.serializer

import com.smallraw.chain.bitcoin.transaction.build.InputToSign
import com.smallraw.chain.bitcoincore.script.Script
import com.smallraw.chain.bitcoincore.stream.BitcoinOutputStream
import com.smallraw.crypto.core.extensions.hexToByteArray

object InputSerializer {
    fun serializeForSignature(
        input: InputToSign,
        script: Script,
        forCurrentInputSignature: Boolean
    ): ByteArray {
        val buffer = BitcoinOutputStream()

        buffer.writeBytes(input.txId.hexToByteArray().reversedArray())
        buffer.writeInt32(input.index)

        if (forCurrentInputSignature) {
            val scriptBytes = script.scriptBytes
            buffer.writeVarInt(scriptBytes.size.toLong())
            buffer.writeBytes(scriptBytes)
        } else {
            buffer.writeVarInt(0L)
        }
        buffer.writeInt32(input.sequence)
        return buffer.toByteArray()
    }
}
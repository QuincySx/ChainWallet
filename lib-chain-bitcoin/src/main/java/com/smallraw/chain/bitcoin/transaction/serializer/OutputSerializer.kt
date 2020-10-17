package com.smallraw.chain.bitcoin.transaction.serializer

import com.smallraw.chain.bitcoin.transaction.build.TransactionOutput
import com.smallraw.chain.bitcoincore.stream.BitcoinOutputStream

object OutputSerializer {
    fun serialize(output: TransactionOutput): ByteArray {
        val buffer = BitcoinOutputStream()
        if (output.address == null) {
            buffer.writeInt64(0)
            val scriptLen = output.pluginScript?.scriptBytes?.size
            buffer.writeVarInt(scriptLen?.toLong() ?: 0)
            buffer.writeBytes(
                output.pluginScript?.scriptBytes ?: byteArrayOf()
            )
        } else {
            buffer.writeInt64(output.value)
            val scriptLen = output.address!!.scriptPubKey().scriptBytes.size
            buffer.writeVarInt(scriptLen.toLong())
            buffer.writeBytes(
                output.address!!.scriptPubKey().scriptBytes
            )
        }
        return buffer.toByteArray()
    }
}
package com.smallraw.chain.bitcoin.transaction.serializer

import com.smallraw.chain.bitcoin.transaction.build.TransactionOutput
import com.smallraw.chain.bitcoincore.stream.BitcoinOutputStream

object OutputSerializer {
    fun serialize(output: TransactionOutput): ByteArray {
        val buffer = BitcoinOutputStream()
        buffer.writeInt64(output.value)
        val scriptLen = output.address.scriptPubKey().scriptBytes.size
        buffer.writeVarInt(scriptLen.toLong())
        buffer.writeBytes(output.address.scriptPubKey().scriptBytes)
        return buffer.toByteArray()
    }
}
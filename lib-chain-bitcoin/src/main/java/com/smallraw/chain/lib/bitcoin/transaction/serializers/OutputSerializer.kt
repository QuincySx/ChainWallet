package com.smallraw.chain.lib.bitcoin.transaction.serializers

import com.smallraw.chain.lib.bitcoin.stream.BitcoinOutputStream
import com.smallraw.chain.lib.bitcoin.transaction.build.TransactionOutput

object OutputSerializer {
    fun serialize(output: TransactionOutput): ByteArray {
        val buffer = BitcoinOutputStream()
        buffer.writeInt64(output.value)
        val scriptLen = output.address.lockingScript.size
        buffer.writeVarInt(scriptLen.toLong())
        buffer.write(output.address.lockingScript)
        return buffer.toByteArray()
    }
}
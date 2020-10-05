package com.smallraw.chain.bitcoin.transaction.serializers

import com.smallraw.chain.bitcoin.stream.ByteWriter
import com.smallraw.chain.bitcoin.transaction.build.TransactionOutput

object OutputSerializer {
    fun serialize(output: TransactionOutput): ByteArray {
        val buffer = ByteWriter()
        buffer.writeInt64(output.value)
        val scriptLen = output.address.lockingScript.size
        buffer.writeVarInt(scriptLen.toLong())
        buffer.write(output.address.lockingScript)
        return buffer.toByteArray()
    }
}
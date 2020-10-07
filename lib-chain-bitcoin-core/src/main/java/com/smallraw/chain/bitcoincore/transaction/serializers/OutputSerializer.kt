package com.smallraw.chain.bitcoincore.transaction.serializers

import com.smallraw.chain.bitcoincore.execptions.BitcoinException
import com.smallraw.chain.bitcoincore.script.Script
import com.smallraw.chain.bitcoincore.stream.BitcoinInputStream
import com.smallraw.chain.bitcoincore.stream.BitcoinOutputStream
import com.smallraw.chain.bitcoincore.transaction.Transaction

object OutputSerializer {
    fun serialize(output: Transaction.Output): ByteArray {
        return BitcoinOutputStream(128).apply {
            writeInt64(output.value)
            val scriptLen = if (output.script == null) 0 else output.script.scriptBytes.size
            writeVarInt(scriptLen.toLong())
            if (scriptLen > 0) {
                output.script?.scriptBytes?.let { write(it) }
            }
        }.toByteArray()
    }

    fun deserialize(bitInput: BitcoinInputStream, i: Int): Transaction.Output {
        val value = bitInput.readInt64()
        val scriptSize = bitInput.readVarInt()
        if (scriptSize < 0 || scriptSize > 10000000) {
            throw BitcoinException(
                BitcoinException.ERR_BAD_FORMAT, "Script size for " +
                        "output " + i +
                        " is strange (" + scriptSize + " bytes)."
            )
        }
        val script = bitInput.readBytes(scriptSize.toInt())
        return Transaction.Output(
            value,
            Script(script)
        )
    }
}
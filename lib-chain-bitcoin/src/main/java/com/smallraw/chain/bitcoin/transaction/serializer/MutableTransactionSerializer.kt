package com.smallraw.chain.bitcoin.transaction.serializer

import com.smallraw.chain.bitcoin.transaction.build.InputToSign
import com.smallraw.chain.bitcoin.transaction.build.MutableTransaction
import com.smallraw.chain.bitcoin.transaction.build.TransactionOutput
import com.smallraw.chain.bitcoincore.stream.BitcoinOutputStream

object MutableTransactionSerializer {
    fun serializeForSignature(
        transaction: MutableTransaction,
        inputsToSign: MutableList<InputToSign>,
        outputs: List<TransactionOutput>,
        inputIndex: Int,
        isWitness: Boolean
    ): ByteArray {
        val buffer = BitcoinOutputStream()
        buffer.writeInt32(transaction.version)
        if (isWitness) {
            // todo
        } else {
            buffer.writeVarInt(inputsToSign.size.toLong())
            inputsToSign.forEachIndexed { index, input ->
                buffer.writeBytes(InputSerializer.serializeForSignature(input, index == inputIndex))
            }

            // outputs
            buffer.writeVarInt(outputs.size.toLong())
            outputs.forEach { buffer.writeBytes(OutputSerializer.serialize(it)) }
        }
        buffer.writeInt32(transaction.lockTime)
        return buffer.toByteArray()
    }
}
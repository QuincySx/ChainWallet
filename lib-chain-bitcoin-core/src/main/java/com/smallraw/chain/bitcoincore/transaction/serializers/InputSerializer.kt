package com.smallraw.chain.bitcoincore.transaction.serializers

import com.smallraw.chain.bitcoincore.script.Chunk
import com.smallraw.chain.bitcoincore.script.Script
import com.smallraw.chain.bitcoincore.stream.BitcoinInputStream
import com.smallraw.chain.bitcoincore.stream.BitcoinOutputStream
import com.smallraw.chain.bitcoincore.transaction.Transaction

object InputSerializer {
    fun serialize(input: Transaction.Input, signed: Boolean = true): ByteArray {
        return BitcoinOutputStream(128).apply {
            writeBytes(input.outPoint.hash.reversedArray())
            writeInt32(input.outPoint.index)
            val scriptLen = if (input.script == null || !signed) {
                0
            } else {
                input.script?.scriptBytes?.size ?: 0
            }.toLong()
            writeVarInt(scriptLen)
            if (scriptLen > 0 && signed) {
                input.script?.scriptBytes?.let { writeBytes(it) }
            }
            writeInt32(input.sequence)
        }.toByteArray()
    }

    fun deserialize(bitInput: BitcoinInputStream): Transaction.Input {
        val outPoint = Transaction.OutPoint(
            bitInput.readBytes(32).reversedArray(),
            bitInput.readInt32()
        )
        val scriptLen = bitInput.readVarInt().toInt()
        val script = bitInput.readBytes(scriptLen)
        val sequence = bitInput.readInt32()
        return Transaction.Input(
            outPoint,
            Script(script),
            sequence
        )
    }

    fun serializeWitness(witness: Transaction.InputWitness): ByteArray {
        return BitcoinOutputStream(128).apply {
            writeVarInt(witness.stackCount().toLong())

            witness.iterator().forEach { data ->
                val bytes = data.toBytes()
                writeVarInt(bytes.size.toLong())
                writeBytes(bytes)
            }
        }.toByteArray()
    }

    fun deserializeWitness(bitInput: BitcoinInputStream): Transaction.InputWitness {
        val stackSize = bitInput.readVarInt().toInt()

        val inputWitness = Transaction.InputWitness(stackSize)

        for (i in 0 until stackSize) {
            val dataSize = bitInput.readVarInt()
            val data = bitInput.readBytes(dataSize.toInt())
            inputWitness.setStack(i, Chunk(data))
        }
        return inputWitness
    }
}
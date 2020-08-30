package com.smallraw.chain.lib.bitcoin.transaction.serializers

import com.smallraw.chain.lib.bitcoin.execptions.BitcoinException
import com.smallraw.chain.lib.bitcoin.stream.BitcoinInputStream
import com.smallraw.chain.lib.bitcoin.stream.BitcoinOutputStream
import com.smallraw.chain.lib.bitcoin.transaction.BTCTransaction
import com.smallraw.chain.lib.bitcoin.transaction.build.InputToSign
import com.smallraw.chain.lib.bitcoin.transaction.build.MutableBTCTransaction
import com.smallraw.chain.lib.bitcoin.transaction.build.TransactionOutput
import com.smallraw.chain.lib.bitcoin.transaction.script.Script
import java.io.EOFException
import java.io.IOException

object TransactionSerializer {
    fun decode(rawBytes: ByteArray): BTCTransaction {
        if (rawBytes == null) {
            throw BitcoinException(BitcoinException.ERR_NO_INPUT, "empty input")
        }
        var bais: BitcoinInputStream? = null
        try {
            bais = BitcoinInputStream(rawBytes)
            val version = bais.readInt32()
            if (version != 1 && version != 2 && version != 3) {
                throw BitcoinException(
                    BitcoinException.ERR_UNSUPPORTED, "Unsupported TX " +
                            "version", version
                )
            }
            val inputsCount = bais.readVarInt().toInt()
            val inputs = Array(inputsCount) { BTCTransaction.Input.default() }
            for (i in 0 until inputsCount) {
                val outPoint = BTCTransaction.OutPoint(
                    bais.readBytes(32).reversedArray(),
                    bais.readInt32()
                )
                val script = bais.readBytes(bais.readVarInt().toInt())
                val sequence = bais.readInt32()
                inputs[i] = BTCTransaction.Input(
                    outPoint,
                    Script(script),
                    sequence
                )
            }
            val outputsCount = bais.readVarInt().toInt()
            val outputs = Array(outputsCount) { BTCTransaction.Output.default() }
            for (i in 0 until outputsCount) {
                val value = bais.readInt64()
                val scriptSize = bais.readVarInt()
                if (scriptSize < 0 || scriptSize > 10000000) {
                    throw BitcoinException(
                        BitcoinException.ERR_BAD_FORMAT, "Script size for " +
                                "output " + i +
                                " is strange (" + scriptSize + " bytes)."
                    )
                }
                val script = bais.readBytes(scriptSize.toInt())
                outputs[i] = BTCTransaction.Output(
                    value,
                    Script(script)
                )
            }
            val lockTime = bais.readInt32()
            return BTCTransaction(
                version,
                lockTime,
                inputs,
                outputs
            )
        } catch (e: EOFException) {
            throw BitcoinException(BitcoinException.ERR_BAD_FORMAT, "TX incomplete")
        } catch (e: IOException) {
            throw IllegalArgumentException("Unable to read TX")
        } catch (e: Error) {
            throw IllegalArgumentException("Unable to read TX: $e")
        } finally {
            if (bais != null) {
                try {
                    bais.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * 序列化交易
     * @param btcTransaction 交易
     * @param signed 序列化签名
     */
    fun serialize(btcTransaction: BTCTransaction, signed: Boolean = true): ByteArray {
        btcTransaction.apply {
            val baos = BitcoinOutputStream()
            try {
                baos.writeInt32(version)
                baos.writeVarInt(inputs.size.toLong())
                for (input in inputs) {
                    baos.write(input.outPoint.hash.reversedArray())
                    baos.writeInt32(input.outPoint.index)

                    val scriptLen =
                        if (input.script == null || !signed) 0 else input.script.bytes.size
                    baos.writeVarInt(scriptLen.toLong())
                    if (scriptLen > 0 && signed) {
                        baos.write(input.script!!.bytes)
                    }

                    baos.writeInt32(input.sequence)
                }
                baos.writeVarInt(outputs.size.toLong())
                for (output in outputs) {
                    baos.writeInt64(output.value)
                    val scriptLen = if (output.script == null) 0 else output.script.bytes.size
                    baos.writeVarInt(scriptLen.toLong())
                    if (scriptLen > 0) {
                        baos.write(output.script.bytes)
                    }
                }
                baos.writeInt32(lockTime)
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    baos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return baos.toByteArray()
        }
    }

    fun serializeForSignature(
        transaction: MutableBTCTransaction,
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
                buffer.write(InputSerializer.serializeForSignature(input, index == inputIndex))
            }

            // outputs
            buffer.writeVarInt(outputs.size.toLong())
            outputs.forEach { buffer.write(OutputSerializer.serialize(it)) }
        }
        buffer.writeInt32(transaction.lockTime)
        return buffer.toByteArray()
    }
}

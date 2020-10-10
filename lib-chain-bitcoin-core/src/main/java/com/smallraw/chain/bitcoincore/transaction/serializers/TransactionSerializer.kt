package com.smallraw.chain.bitcoincore.transaction.serializers

import com.smallraw.chain.bitcoincore.execptions.BitcoinException
import com.smallraw.chain.bitcoincore.script.Script
import com.smallraw.chain.bitcoincore.script.SigHash
import com.smallraw.chain.bitcoincore.stream.BitcoinInputStream
import com.smallraw.chain.bitcoincore.stream.BitcoinOutputStream
import com.smallraw.chain.bitcoincore.transaction.Transaction
import com.smallraw.chain.lib.core.crypto.Sha256
import com.smallraw.chain.lib.core.extensions.hexToByteArray
import java.io.EOFException
import java.io.IOException
import kotlin.experimental.and

object TransactionSerializer {
    fun deserialize(rawBytes: ByteArray): Transaction {
        if (rawBytes.isEmpty()) {
            throw BitcoinException(BitcoinException.ERR_NO_INPUT, "empty input")
        }
        var bais: BitcoinInputStream? = null
        try {
            bais = BitcoinInputStream(rawBytes)
            val version = bais.readInt32()
            if (version != 1 && version != 2 && version != 3) {
                throw BitcoinException(
                    BitcoinException.ERR_UNSUPPORTED, "Unsupported TX version", version
                )
            }

            var hasSegwit = false

            bais.mark() // 标记 mark 位置
            if (bais.readByte() == 0 && bais.readByte() == 1) {
                hasSegwit = true
            } else {
                // 还原到 mark 标记的位置
                bais.reset()
            }

            // deserialize inputs data
            val inputsCount = bais.readVarInt().toInt()
            val inputs = Array(inputsCount) { Transaction.Input.default() }
            for (i in 0 until inputsCount) {
                inputs[i] = InputSerializer.deserialize(bais)
            }

            // deserialize output data
            val outputsCount = bais.readVarInt().toInt()
            val outputs = Array(outputsCount) { Transaction.Output.default() }
            for (i in 0 until outputsCount) {
                outputs[i] = OutputSerializer.deserialize(bais, i)
            }

            //  deserialize witness data
            if (hasSegwit) {
                inputs.forEach {
                    it.witness = InputSerializer.deserializeWitness(bais)
                }
            }

            val lockTime = bais.readInt32()
            return Transaction(
                inputs,
                outputs,
                version,
                lockTime
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
     * @param transaction 交易
     * @param withSigned 序列化是否包含签名
     * @param withWitness 序列化是否包含隔离见证签名
     */
    fun serialize(
        transaction: Transaction,
        withSigned: Boolean = true,
        withWitness: Boolean = withSigned
    ): ByteArray {
        transaction.apply {
            val baos = BitcoinOutputStream(256)
            try {
                baos.writeInt32(version)

                if (hasSegwit() && withWitness) {
                    baos.writeByte(0) // marker 0x00
                    baos.writeByte(1) // flag 0x01
                }

                // serialize inputs data
                baos.writeVarInt(inputs.size.toLong())
                for (input in inputs) {
                    baos.writeBytes(InputSerializer.serialize(input, withSigned))
                }

                // serialize outputs data
                baos.writeVarInt(outputs.size.toLong())
                for (output in outputs) {
                    baos.writeBytes(OutputSerializer.serialize(output))
                }

                // serialize witness data
                if (hasSegwit() && withWitness) {
                    inputs.forEach {
                        baos.writeBytes(InputSerializer.serializeWitness(it.witness))
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

    fun hashForSignature(
        transaction: Transaction,
        txinIndex: Int,
        script: Script,
        sigHash: Byte = SigHash.ALL
    ): ByteArray {
        transaction.copy().apply {
            // clear transaction inputs script
            for (input in inputs) {
                input.script = null
            }

            inputs[txinIndex].script = script

            if (sigHash and 0x1F == SigHash.NONE) {
                outputs = arrayOf()
                inputs.filterIndexed { index, _ ->
                    index != txinIndex
                }.map {
                    it.sequence = Transaction.EMPTY_TX_SEQUENCE
                }
            } else if (sigHash and 0x1F == SigHash.SINGLE) {
                if (txinIndex >= outputs.size) {
                    return "0100000000000000000000000000000000000000000000000000000000000000".hexToByteArray()
                }

                outputs = outputs.copyOfRange(0, txinIndex + 1)

                for (index in 1 until txinIndex) {
                    outputs[index] = Transaction.Output(Transaction.NEGATIVE_SATOSHI, null)
                }

                inputs.filterIndexed { index, _ ->
                    index != txinIndex
                }.map {
                    it.sequence = Transaction.EMPTY_TX_SEQUENCE
                }
            }

            if (sigHash and SigHash.ANYONECANPAY == SigHash.ANYONECANPAY) {
                inputs = arrayOf(inputs[txinIndex])
            }

            val serialize = serialize(this) + byteArrayOf(sigHash, 0, 0, 0)

            return Sha256.doubleSha256(serialize)
        }
    }

    fun hashForWitnessSignature(
        transaction: Transaction,
        txinIndex: Int,
        script: Script,
        prevAmount: Long,
        sigHash: Byte = SigHash.ALL
    ): ByteArray {
        var hashPrevouts = ByteArray(32) { 0 }
        var hashSequence = ByteArray(32) { 0 }
        var hashOutputs = ByteArray(32) { 0 }

        val basicSigHashType = sigHash and 0x1F
        val anyoneCanPay = sigHash.and(SigHash.ANYONECANPAY) == SigHash.ANYONECANPAY
        val signAll = (basicSigHashType != SigHash.SINGLE) && (basicSigHashType != SigHash.NONE)

        transaction.copy().apply {
            // Hash all input
            if (!anyoneCanPay) {
                val bosHashPrevouts = BitcoinOutputStream(40 * inputs.size)
                inputs.forEach {
                    bosHashPrevouts.writeBytes(it.outPoint.hash.reversedArray())
                    bosHashPrevouts.writeInt32(it.outPoint.index)
                }
                hashPrevouts = Sha256.doubleSha256(bosHashPrevouts.toByteArray())
            }

            // Hash all input sequence
            if (!anyoneCanPay && signAll) {
                val bosSequence = BitcoinOutputStream(8 * inputs.size)
                inputs.forEach {
                    bosSequence.writeInt32(it.sequence)
                }
                hashSequence = Sha256.doubleSha256(bosSequence.toByteArray())
            }

            if (signAll) {
                val bosHashOutputs = BitcoinOutputStream(64 * outputs.size)
                outputs.forEach { output ->
                    bosHashOutputs.writeBytes(OutputSerializer.serialize(output))
                }
                hashOutputs = Sha256.doubleSha256(bosHashOutputs.toByteArray())
            } else if (basicSigHashType == SigHash.SINGLE && txinIndex < outputs.size) {
                hashOutputs = Sha256.doubleSha256(OutputSerializer.serialize(outputs[txinIndex]))
            }

            val bos = BitcoinOutputStream(256)
            bos.writeInt32(version)
            bos.writeBytes(hashPrevouts)
            bos.writeBytes(hashSequence)
            bos.writeBytes(inputs[txinIndex].outPoint.hash.reversedArray())
            bos.writeInt32(inputs[txinIndex].outPoint.index)
            val scriptBytes = script.scriptBytes
            bos.writeVarInt(scriptBytes.size.toLong())
            bos.writeBytes(scriptBytes)
            bos.writeInt64(prevAmount)
            bos.writeInt32(inputs[txinIndex].sequence)
            bos.writeBytes(hashOutputs)
            bos.writeInt32(lockTime)
            bos.writeInt32(0x000000ff and sigHash.toInt())

            val serialize = bos.toByteArray()

            return Sha256.doubleSha256(serialize)
        }
    }
}
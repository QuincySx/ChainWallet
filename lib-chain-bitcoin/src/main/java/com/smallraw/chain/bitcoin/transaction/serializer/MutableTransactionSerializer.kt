package com.smallraw.chain.bitcoin.transaction.serializer

import com.smallraw.chain.bitcoin.transaction.build.InputToSign
import com.smallraw.chain.bitcoin.transaction.build.MutableTransaction
import com.smallraw.chain.bitcoin.transaction.build.TransactionOutput
import com.smallraw.chain.bitcoincore.script.Script
import com.smallraw.chain.bitcoincore.script.SigHash
import com.smallraw.chain.bitcoincore.stream.BitcoinOutputStream
import com.smallraw.chain.bitcoincore.transaction.Transaction
import com.smallraw.crypto.core.crypto.Sha256
import com.smallraw.crypto.core.extensions.hexToByteArray
import kotlin.experimental.and

object MutableTransactionSerializer {
    fun serializeForSignature(
        transaction: MutableTransaction,
        inputsToSign: List<InputToSign>,
        outputs: List<TransactionOutput>,
        inputIndex: Int,
        script: Script,
        isWitness: Boolean,
        sigHash: Byte
    ): ByteArray {
        val anyoneCanPay = sigHash.and(SigHash.ANYONECANPAY) == SigHash.ANYONECANPAY
        val buffer = BitcoinOutputStream(256)
        buffer.writeInt32(transaction.version)
        if (isWitness) {
            var hashPrevouts = ByteArray(32) { 0 }
            var hashSequence = ByteArray(32) { 0 }
            var hashOutputs = ByteArray(32) { 0 }

            val basicSigHashType = sigHash and 0x1F
            val signAll = (basicSigHashType != SigHash.SINGLE) && (basicSigHashType != SigHash.NONE)

            // Hash all input
            if (!anyoneCanPay) {
                val bosHashPrevouts = BitcoinOutputStream(40 * inputsToSign.size)
                inputsToSign.forEach {
                    bosHashPrevouts.writeBytes(it.txId.hexToByteArray().reversedArray())
                    bosHashPrevouts.writeInt32(it.index)
                }
                hashPrevouts = Sha256.doubleSha256(bosHashPrevouts.toByteArray())
            }

            // Hash all input sequence
            if (!anyoneCanPay && signAll) {
                val bosSequence = BitcoinOutputStream(8 * inputsToSign.size)
                inputsToSign.forEach {
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
            } else if (basicSigHashType == SigHash.SINGLE && inputIndex < outputs.size) {
                hashOutputs = Sha256.doubleSha256(OutputSerializer.serialize(outputs[inputIndex]))
            }

            buffer.writeBytes(hashPrevouts)
            buffer.writeBytes(hashSequence)
            buffer.writeBytes(inputsToSign[inputIndex].txId.hexToByteArray().reversedArray())
            buffer.writeInt32(inputsToSign[inputIndex].index)
            val scriptBytes = script.scriptBytes
            buffer.writeVarInt(scriptBytes.size.toLong())
            buffer.writeBytes(scriptBytes)
            buffer.writeInt64(inputsToSign[inputIndex].value)
            buffer.writeInt32(inputsToSign[inputIndex].sequence)
            buffer.writeBytes(hashOutputs)
        } else {
            when (sigHash and 0x1F) {
                SigHash.NONE -> {
                    buffer.writeVarInt(inputsToSign.size.toLong())
                    inputsToSign.forEachIndexed { index, input ->
                        buffer.writeBytes(input.txId.hexToByteArray().reversedArray())
                        buffer.writeInt32(input.index)
                        if (index == inputIndex) {
                            val scriptBytes = script.scriptBytes
                            buffer.writeVarInt(scriptBytes.size.toLong())
                            buffer.writeBytes(scriptBytes)
                            buffer.writeInt32(input.sequence)
                        } else {
                            buffer.writeVarInt(0L)
                            buffer.writeInt32(Transaction.EMPTY_TX_SEQUENCE)
                        }
                    }
                    buffer.writeVarInt(0)
                }
                SigHash.SINGLE -> {
                    if (inputIndex >= outputs.size) {
                        buffer.writeBytes("0100000000000000000000000000000000000000000000000000000000000000".hexToByteArray())
                        return "".hexToByteArray()
                    } else {
                        if (anyoneCanPay) {
                            buffer.writeVarInt(1L)
                            buffer.writeBytes(
                                InputSerializer.serializeForSignature(
                                    inputsToSign[inputIndex],
                                    script,
                                    true
                                )
                            )
                        } else {
                            buffer.writeVarInt(inputsToSign.size.toLong())
                            inputsToSign.forEachIndexed { index, input ->
                                buffer.writeBytes(input.txId.hexToByteArray().reversedArray())
                                buffer.writeInt32(input.index)
                                if (index == inputIndex) {
                                    val scriptBytes = script.scriptBytes
                                    buffer.writeVarInt(scriptBytes.size.toLong())
                                    buffer.writeBytes(scriptBytes)
                                    buffer.writeInt32(input.sequence)
                                } else {
                                    buffer.writeVarInt(0L)
                                    buffer.writeInt32(Transaction.EMPTY_TX_SEQUENCE)
                                }
                            }
                        }
                        buffer.writeVarInt(outputs.size.toLong())
                        for (index in 0 until inputIndex - 1) {
                            buffer.writeInt64(-1)
                            buffer.writeVarInt(0)
                        }
                        val output = outputs[inputIndex]
                        if (output.address == null) {
                            buffer.writeInt64(0)
                            val scriptLen = output.pluginScript?.scriptBytes?.size
                            buffer.writeVarInt(scriptLen?.toLong() ?: 0)
                            buffer.writeBytes(output.pluginScript?.scriptBytes ?: byteArrayOf())
                        } else {
                            buffer.writeInt64(output.value)
                            val scriptLen = output.address!!.scriptPubKey().scriptBytes.size
                            buffer.writeVarInt(scriptLen.toLong())
                            buffer.writeBytes(output.address!!.scriptPubKey().scriptBytes)
                        }
                    }
                }
                SigHash.ALL -> {
                    if (anyoneCanPay) {
                        buffer.writeVarInt(1L)
                        buffer.writeBytes(
                            InputSerializer.serializeForSignature(
                                inputsToSign[inputIndex],
                                script,
                                true
                            )
                        )
                    } else {
                        buffer.writeVarInt(inputsToSign.size.toLong())
                        inputsToSign.forEachIndexed { index, input ->
                            buffer.writeBytes(
                                InputSerializer.serializeForSignature(
                                    input,
                                    script,
                                    index == inputIndex
                                )
                            )
                        }
                    }
                    // outputs
                    buffer.writeVarInt(outputs.size.toLong())
                    outputs.forEach { buffer.writeBytes(OutputSerializer.serialize(it)) }
                }
            }
        }
        buffer.writeInt32(transaction.lockTime)
        buffer.writeInt32(0x000000ff and sigHash.toInt())
        return buffer.toByteArray()
    }
}
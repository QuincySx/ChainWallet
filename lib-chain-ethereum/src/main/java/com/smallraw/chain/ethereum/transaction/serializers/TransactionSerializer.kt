package com.smallraw.chain.ethereum.transaction.serializers

import com.smallraw.chain.ethereum.Address
import com.smallraw.chain.ethereum.Signature
import com.smallraw.chain.ethereum.network.BaseNetwork
import com.smallraw.chain.ethereum.rpl.RLP
import com.smallraw.chain.ethereum.transaction.Transaction
import com.smallraw.crypto.core.crypto.Keccak
import com.smallraw.crypto.core.extensions.toBigInteger
import com.smallraw.crypto.core.extensions.toInt
import com.smallraw.crypto.core.extensions.toLong

object TransactionSerializer {
    private const val MAX_CHAIN_ID = (0xFFFF_FFFF - 36) / 2

    fun serialize(rawTransaction: Transaction, network: BaseNetwork): ByteArray {
        val v = when {
            rawTransaction.signature == null -> {
                // 普通交易计算未签名 hash 是需要放入网络 ID
                network.id.toByte()
            }
            rawTransaction.isEIP1559() -> (rawTransaction.signature!!.v() - 27).toByte()
            else -> {
                if (network.id > MAX_CHAIN_ID) {
                    (rawTransaction.signature!!.v() - 27).toByte()
                } else {
                    (rawTransaction.signature!!.v() + 2 * network.id + 8).toByte()
                }
            }
        }

        return if (rawTransaction.isEIP1559()) {
            if (rawTransaction.maxPriorityFeePerGas == null) throw RuntimeException("maxPriorityFeePerGas Parameters required")
            if (rawTransaction.maxFeePerGas == null) throw RuntimeException("maxFeePerGas Parameters required")
            if (rawTransaction.signature == null) {
                byteArrayOf(2) + RLP.encodeList(
                    RLP.encodeLong(network.id),
                    RLP.encodeLong(rawTransaction.nonce),
                    RLP.encodeBigInteger(rawTransaction.maxPriorityFeePerGas),
                    RLP.encodeBigInteger(rawTransaction.maxFeePerGas),
                    RLP.encodeLong(rawTransaction.gasLimit),
                    RLP.encodeElement(rawTransaction.to.raw),
                    RLP.encodeBigInteger(rawTransaction.value),
                    RLP.encodeElement(rawTransaction.data),
                    RLP.encodeList()
                )
            } else {
                byteArrayOf(2) + RLP.encodeList(
                    RLP.encodeLong(network.id),
                    RLP.encodeLong(rawTransaction.nonce),
                    RLP.encodeBigInteger(rawTransaction.maxPriorityFeePerGas),
                    RLP.encodeBigInteger(rawTransaction.maxFeePerGas),
                    RLP.encodeLong(rawTransaction.gasLimit),
                    RLP.encodeElement(rawTransaction.to.raw),
                    RLP.encodeBigInteger(rawTransaction.value),
                    RLP.encodeElement(rawTransaction.data),
                    RLP.encodeList(),
                    RLP.encodeByte(v),
                    RLP.encodeElement(rawTransaction.signature?.r() ?: byteArrayOf()),
                    RLP.encodeElement(rawTransaction.signature?.s() ?: byteArrayOf())
                )
            }
        } else {
            if (rawTransaction.gasPrice == null) throw RuntimeException("gasPrice Parameters required")
            RLP.encodeList(
                RLP.encodeLong(rawTransaction.nonce),
                RLP.encodeLong(rawTransaction.gasPrice),
                RLP.encodeLong(rawTransaction.gasLimit),
                RLP.encodeElement(rawTransaction.to.raw),
                RLP.encodeBigInteger(rawTransaction.value),
                RLP.encodeElement(rawTransaction.data),
                RLP.encodeByte(v),
                RLP.encodeElement(rawTransaction.signature?.r() ?: byteArrayOf()),
                RLP.encodeElement(rawTransaction.signature?.s() ?: byteArrayOf())
            )
        }

    }

    fun hashForSignature(rawTransaction: Transaction, network: BaseNetwork): ByteArray {
        val hexTransaction = serialize(rawTransaction, network)
        return Keccak.sha256(hexTransaction)
    }

    fun deserialize(hexTransaction: ByteArray): Transaction? {
        val transactionType = RLP.decode(hexTransaction, 0).decoded

        if (transactionType is Byte && transactionType < 0x80) {
            // eip-2718
            val decode = (RLP.decode(hexTransaction, 1).decoded as Array<Any>)
            val networkId = decode[0].checkByteArray().toLong()
            val nonce = decode[1].checkByteArray().toLong()
            val maxPriorityFeePerGas = decode[2].checkByteArray().toLong()
            val maxFeePerGas = decode[3].checkByteArray().toLong()
            val gasLimit = decode[4].checkByteArray().toLong()
            val to = decode[5].checkByteArray()
            val value = decode[6].checkByteArray().toBigInteger()
            val data = decode[7].checkByteArray()
            val dataList = decode[8].checkByteArray()
            val v = decode.getOrNull(9)?.checkByteArray()?.toInt()
            val r = decode.getOrNull(10)?.checkByteArray()
            val s = decode.getOrNull(11)?.checkByteArray()

            val sign = if (v != null && r != null && s != null) {
                Signature(r, s, v)
            } else null

            return Transaction.createTransaction(
                nonce,
                maxPriorityFeePerGas.toBigInteger(),
                maxFeePerGas.toBigInteger(),
                gasLimit,
                Address(to),
                value,
                data,
                sign,
                transactionType.toLong()
            )
        } else {
            val decode = (RLP.decode(hexTransaction, 0).decoded as Array<Any>)
            val nonce = decode[0].checkByteArray().toLong()
            val gasPrice = decode[1].checkByteArray().toLong()
            val gasLimit = decode[2].checkByteArray().toLong()
            val to = decode[3].checkByteArray()
            val value = decode[4].checkByteArray().toBigInteger()
            val data = decode[5].checkByteArray()
            val v = decode.getOrNull(6)?.checkByteArray()?.toInt()
            val r = decode.getOrNull(7)?.checkByteArray()
            val s = decode.getOrNull(8)?.checkByteArray()

            val sign = if (v != null && r != null && s != null) {
                Signature(r, s, v)
            } else null

            return Transaction.createTransaction(
                nonce,
                gasPrice,
                gasLimit,
                Address(to),
                value,
                data,
                sign
            )
        }
    }

    private fun Any.checkByteArray(): ByteArray {
        return when (this) {
            is String -> byteArrayOf()
            is ByteArray -> this
            else -> byteArrayOf()
        }
    }
}
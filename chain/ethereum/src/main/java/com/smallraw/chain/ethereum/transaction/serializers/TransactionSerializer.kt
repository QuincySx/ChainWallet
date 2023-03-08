package com.smallraw.chain.ethereum.transaction.serializers

import com.smallraw.chain.ethereum.Address
import com.smallraw.chain.ethereum.Signature
import com.smallraw.chain.ethereum.network.BaseNetwork
import com.smallraw.chain.ethereum.rlp.*
import com.smallraw.chain.ethereum.transaction.Transaction
import com.smallraw.crypto.core.crypto.Keccak
import java.math.BigInteger

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
            if (rawTransaction.transactionType == null) throw RuntimeException("transactionType Parameters required")
            if (rawTransaction.maxPriorityFeePerGas == null) throw RuntimeException("maxPriorityFeePerGas Parameters required")
            if (rawTransaction.maxFeePerGas == null) throw RuntimeException("maxFeePerGas Parameters required")
            var rlpList = listOf(
                network.id.toRLP(),
                rawTransaction.nonce.toRLP(),
                rawTransaction.maxPriorityFeePerGas.toRLP(),
                rawTransaction.maxFeePerGas.toRLP(),
                rawTransaction.gasLimit.toRLP(),
                rawTransaction.to.raw.toRLP(),
                rawTransaction.value.toRLP(),
                rawTransaction.data.toRLP(),
                RLPList(emptyList()),
            )

            if (rawTransaction.signature != null) {
                val signatureRLPList = listOf(
                    BigInteger.valueOf(v.toLong()).toRLP(),
                    rawTransaction.signature?.r().toRLP(),
                    rawTransaction.signature?.s().toRLP(),
                )
                rlpList = rlpList.plus(signatureRLPList)
            }
            rawTransaction.transactionType.and(0xff).toByte().toRLP().encode() + rlpList.toRLP()
                .encode()
        } else {
            if (rawTransaction.gasPrice == null) throw RuntimeException("gasPrice Parameters required")
            listOf(
                rawTransaction.nonce.toRLP(),
                rawTransaction.gasPrice.toRLP(),
                rawTransaction.gasLimit.toRLP(),
                rawTransaction.to.raw.toRLP(),
                rawTransaction.value.toRLP(),
                rawTransaction.data.toRLP(),
                BigInteger.valueOf(v.toLong()).toRLP(),
                rawTransaction.signature?.r().toRLP(),
                rawTransaction.signature?.s().toRLP(),
            ).toRLP().encode()
        }
    }

    fun hashForSignature(rawTransaction: Transaction, network: BaseNetwork): ByteArray {
        val hexTransaction = serialize(rawTransaction, network)
        return Keccak.sha256(hexTransaction)
    }

    fun deserialize(hexTransaction: ByteArray): Transaction? {
        val rlpElement = hexTransaction.decodeRLP()

        if (rlpElement is RLPItem && rlpElement.bytes.isNotEmpty() && rlpElement.bytes.first() < 0x80) {
            // eip-2718
            val transactionType = rlpElement
            val rlpElementPayload = hexTransaction.decodeRLP(1)
            if (rlpElementPayload !is RLPList) {
                return null
            }
            val networkId = (rlpElementPayload[0] as RLPItem).toLongFromRLP()
            val nonce = (rlpElementPayload[1] as RLPItem).toLongFromRLP()
            val maxPriorityFeePerGas = (rlpElementPayload[2] as RLPItem).toLongFromRLP()
            val maxFeePerGas = (rlpElementPayload[3] as RLPItem).toLongFromRLP()
            val gasLimit = (rlpElementPayload[4] as RLPItem).toLongFromRLP()
            val to = (rlpElementPayload[5] as RLPItem).bytes
            val value = (rlpElementPayload[6] as RLPItem).toBigIntegerFromRLP()
            val data = (rlpElementPayload[7] as RLPItem).bytes
            val dataList = (rlpElementPayload[8] as RLPList)
            val v = (rlpElementPayload.getOrNull(9) as RLPItem?)?.toBigIntegerFromRLP()?.toInt()
            val r = (rlpElementPayload.getOrNull(10) as RLPItem?)?.bytes
            val s = (rlpElementPayload.getOrNull(11) as RLPItem?)?.bytes

            val sign = if (v != null && r != null && s != null) {
                Signature(r, s, v + 27)
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
                transactionType.toLongFromRLP()
            )
        } else {
            if (rlpElement !is RLPList) {
                return null
            }

            val nonce = (rlpElement[0] as RLPItem).toLongFromRLP()
            val gasPrice = (rlpElement[1] as RLPItem).toLongFromRLP()
            val gasLimit = (rlpElement[2] as RLPItem).toLongFromRLP()
            val to = (rlpElement[3] as RLPItem).bytes
            val value = (rlpElement[4] as RLPItem).toBigIntegerFromRLP()
            val data = (rlpElement[5] as RLPItem).bytes
            val v = (rlpElement.getOrNull(6) as RLPItem?)?.toBigIntegerFromRLP()?.toInt()
            val r = (rlpElement.getOrNull(7) as RLPItem?)?.bytes
            val s = (rlpElement.getOrNull(8) as RLPItem?)?.bytes

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
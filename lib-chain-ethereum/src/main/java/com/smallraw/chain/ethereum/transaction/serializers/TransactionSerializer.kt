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
    fun serialize(rawTransaction: Transaction, network: BaseNetwork): ByteArray {
        val v = if (rawTransaction.signature == null) {
            network.id.toByte()
        } else {
            (rawTransaction.signature!!.v()
                .toByte() + if (network.id == 0) 27 else (35 + 2 * network.id)).toByte()
        }
        return RLP.encodeList(
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

    fun hashForSignature(rawTransaction: Transaction, network: BaseNetwork): ByteArray {
        val hexTransaction = serialize(rawTransaction, network)
        return Keccak.sha256(hexTransaction)
    }

    fun deserialize(hexTransaction: ByteArray): Transaction {
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

        return Transaction(nonce, gasPrice, gasLimit, Address(to), value, data, sign)
    }

    private fun Any.checkByteArray(): ByteArray {
        return when (this) {
            is String -> byteArrayOf()
            is ByteArray -> this
            else -> byteArrayOf()
        }
    }
}
package com.smallraw.chain.ethereum.transaction

import com.smallraw.chain.ethereum.Address
import com.smallraw.chain.ethereum.Signature
import com.smallraw.chain.ethereum.extensions.toHexPrefix
import java.math.BigInteger

data class Transaction(
    val transactionType: Long? = null,
    val nonce: Long,
    val gasPrice: Long?,
    val gasLimit: Long,
    val to: Address,
    val value: BigInteger,
    val data: ByteArray = ByteArray(0),
    val maxPriorityFeePerGas: BigInteger?,
    val maxFeePerGas: BigInteger?,
    var signature: Signature? = null
) {
    companion object {
        /**
         * Legacy tx
         */
        fun createTransaction(
            nonce: Long,
            gasPrice: Long,
            gasLimit: Long,
            to: Address,
            value: BigInteger,
            data: ByteArray = ByteArray(0),
            signature: Signature? = null
        ): Transaction {
            return Transaction(
                null,
                nonce,
                gasPrice,
                gasLimit,
                to,
                value,
                data,
                null,
                null,
                signature
            )
        }

        /**
         * eip1159 tx
         */
        fun createTransaction(
            nonce: Long,
            maxPriorityFeePerGas: BigInteger?,
            maxFeePerGas: BigInteger?,
            gasLimit: Long,
            to: Address,
            value: BigInteger,
            data: ByteArray = ByteArray(0),
            signature: Signature? = null,
            transactionType: Long? = 2
        ): Transaction {
            return Transaction(
                transactionType,
                nonce,
                null,
                gasLimit,
                to,
                value,
                data,
                maxPriorityFeePerGas,
                maxFeePerGas,
                signature
            )
        }
    }

    override fun toString(): String {
        if (isEIP1559()) {
            return "EIP2718Transaction [transactionType: $transactionType; nonce: $nonce; maxPriorityFeePerGas: $maxPriorityFeePerGas; maxFeePerGas: $maxFeePerGas; gasLimit: $gasLimit; to: $to; value: $value; data: ${data.toHexPrefix()}; r: ${
                signature?.r()?.toHexPrefix()
            }; s: ${signature?.s()?.toHexPrefix()}; v: ${signature?.v() ?: 0}]"
        } else {
            return "Transaction [nonce: $nonce; gasPrice: $gasPrice; gasLimit: $gasLimit; to: $to; value: $value; data: ${data.toHexPrefix()}; r: ${
                signature?.r()?.toHexPrefix()
            }; s: ${signature?.s()?.toHexPrefix()}; v: ${signature?.v() ?: 0}]"
        }
    }

    fun isEIP1559() = gasPrice == null && maxPriorityFeePerGas != null && maxFeePerGas != null
}
package com.smallraw.chain.ethereum.transaction

import com.smallraw.chain.ethereum.Address
import com.smallraw.chain.ethereum.Signature
import com.smallraw.chain.ethereum.extensions.toHexPrefix
import java.math.BigInteger

class Transaction(
    val nonce: Long,
    val gasPrice: Long,
    val gasLimit: Long,
    val to: Address,
    val value: BigInteger,
    val data: ByteArray = ByteArray(0),
    var signature: Signature? = null
) {

    override fun toString(): String {
        return "Transaction [nonce: $nonce; gasPrice: $gasPrice; gasLimit: $gasLimit; to: $to; value: $value; data: ${data.toHexPrefix()}; r: ${
            signature?.r()?.toHexPrefix()
        }; s: ${signature?.s()?.toHexPrefix()}; v: ${signature?.v() ?: 0}]"
    }
}
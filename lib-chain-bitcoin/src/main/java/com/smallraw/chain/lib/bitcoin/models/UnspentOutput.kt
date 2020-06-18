package com.smallraw.chain.lib.bitcoin.models

import com.smallraw.chain.lib.bitcoin.BitcoinAddress

data class UnspentOutput(
    val confirmations: Long,
    val height: Long,
    val txid: String,
    val value: String,
    val vout: Int
)

data class UnspentOutputWithAddress(
    val address: BitcoinAddress,
    val confirmations: Long,
    val height: Long,
    val txid: String,
    val value: String,
    val vout: Int
)
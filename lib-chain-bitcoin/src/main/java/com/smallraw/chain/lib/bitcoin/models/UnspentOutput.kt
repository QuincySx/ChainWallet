package com.smallraw.chain.lib.bitcoin.models

import com.smallraw.chain.lib.bitcoin.Bitcoin

data class UnspentOutput(
    val confirmations: Long,
    val height: Long,
    val txid: String,
    val value: String,
    val vout: Int
)

data class UnspentOutputWithAddress(
    val address: Bitcoin.Address,
    val confirmations: Long,
    val height: Long,
    val txid: String,
    val value: String,
    val vout: Int
)
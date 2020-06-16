package com.smallraw.chain.lib.bitcoin.models

data class UnspentOutput(
    val confirmations: Long,
    val height: Long,
    val txid: String,
    val value: String,
    val vout: Int
)
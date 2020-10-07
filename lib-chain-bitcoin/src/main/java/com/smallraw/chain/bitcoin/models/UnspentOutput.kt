package com.smallraw.chain.bitcoin.models

import com.smallraw.chain.bitcoin.Bitcoin

data class UnspentOutput(
    val confirmations: Long,
    val height: Long,
    val txid: String,
    val value: String,
    val vout: Int,
    // output 锁定脚本：ScriptInput
    var lockScript: ByteArray? = null
)

data class UnspentOutputWithAddress(
    val address: Bitcoin.Address,
    val confirmations: Long,
    val height: Long,
    val txid: String,
    val value: String,
    val vout: Int,
    // output 锁定脚本：ScriptInput
    var lockScript: ByteArray? = null
)
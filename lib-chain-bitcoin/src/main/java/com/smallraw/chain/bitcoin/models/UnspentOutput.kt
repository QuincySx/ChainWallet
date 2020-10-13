package com.smallraw.chain.bitcoin.models

import com.smallraw.chain.bitcoincore.address.Address
import com.smallraw.chain.bitcoincore.script.ScriptType

data class UnspentOutput(
    val address: Address,
    val confirmations: Long,
    val height: Long,
    val txid: String,
    val value: Long,
    val vout: Int,
    var scriptType: ScriptType = ScriptType.UNKNOWN,
    // output 赎回脚本：ScriptInput
    var redeemScript: ByteArray? = null
)
package com.smallraw.chain.bitcoin.unspentOutput

import com.smallraw.chain.bitcoin.models.UnspentOutput

/**
 * 未使用的 UTXO 提供器
 */
interface IUnspentOutputProvider {
    fun nextUtxoList(): List<UnspentOutput>
}
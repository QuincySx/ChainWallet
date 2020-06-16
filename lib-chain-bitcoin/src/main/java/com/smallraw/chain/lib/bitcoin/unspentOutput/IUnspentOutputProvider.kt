package com.smallraw.chain.lib.bitcoin.unspentOutput

import com.smallraw.chain.lib.bitcoin.models.UnspentOutput

interface IUnspentOutputProvider {
    fun nextUtxoList(): List<UnspentOutput>
}
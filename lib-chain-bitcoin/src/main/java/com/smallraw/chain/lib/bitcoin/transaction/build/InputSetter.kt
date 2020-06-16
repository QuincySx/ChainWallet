package com.smallraw.chain.lib.bitcoin.transaction.build

import com.smallraw.chain.lib.bitcoin.models.UnspentOutput

class InputSetter {
    fun setInputs(
        mutableBTCTransaction: MutableBTCTransaction,
        unspentOutputs: List<UnspentOutput>
    ) {
        val inputs = mutableListOf<InputToSign>()
        unspentOutputs.forEach {
            inputs.add(
                InputToSign(
                    it.txid,
                    it.vout
                )
            )
        }
        mutableBTCTransaction.inputsToSign.addAll(inputs)
    }
}
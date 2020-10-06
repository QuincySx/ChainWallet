package com.smallraw.chain.bitcoin.transaction.build

import com.smallraw.chain.bitcoin.models.UnspentOutputWithAddress

class InputSetter {
    fun setInputs(
        mutableTransaction: MutableTransaction,
        unspentOutputWiths: List<UnspentOutputWithAddress>
    ) {
        val inputs = mutableListOf<InputToSign>()
        unspentOutputWiths.forEach {
            inputs.add(
                InputToSign(
                    it.address,
                    it.txid,
                    it.vout
                )
            )
        }
        mutableTransaction.inputsToSign.addAll(inputs)
    }
}
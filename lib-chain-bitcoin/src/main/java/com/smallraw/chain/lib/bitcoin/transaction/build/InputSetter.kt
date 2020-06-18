package com.smallraw.chain.lib.bitcoin.transaction.build

import com.smallraw.chain.lib.bitcoin.models.UnspentOutputWithAddress

class InputSetter {
    fun setInputs(
        mutableBTCTransaction: MutableBTCTransaction,
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
        mutableBTCTransaction.inputsToSign.addAll(inputs)
    }
}
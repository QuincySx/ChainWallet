package com.smallraw.chain.bitcoin.transaction.build

import com.smallraw.chain.bitcoin.models.UnspentOutputWithAddress
import com.smallraw.chain.bitcoin.transaction.script.Script

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
                    it.vout,
                    redeemScript = it.lockScript?.let { it1 -> Script(it1) }
                )
            )
        }
        mutableTransaction.inputsToSign.addAll(inputs)
    }
}
package com.smallraw.chain.bitcoin.transaction.build

import com.smallraw.chain.bitcoin.models.UnspentOutput
import com.smallraw.chain.bitcoin.transaction.DustCalculator
import com.smallraw.chain.bitcoin.transaction.TransactionSizeCalculator
import com.smallraw.chain.bitcoincore.script.Script

class InputSetter(
    private val transactionSizeCalculator: TransactionSizeCalculator? = null,
    private val dustCalculator: DustCalculator? = null,
) {
    fun setInputs(
        mutableTransaction: MutableTransaction,
        unspentOutputWiths: List<UnspentOutput>
    ) {
        val inputs = mutableListOf<InputToSign>()
        unspentOutputWiths.forEach {
            inputs.add(
                InputToSign(
                    it.address,
                    it.txid,
                    it.vout,
                    redeemScript = it.redeemScript?.let { it1 -> Script(it1) }
                )
            )
        }
        mutableTransaction.inputsToSign.addAll(inputs)
    }
}
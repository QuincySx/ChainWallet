package com.smallraw.chain.lib.bitcoin.transaction.build

import com.smallraw.chain.lib.bitcoin.BitcoinP2PKHAddress
import com.smallraw.chain.lib.bitcoin.models.UnspentOutput

data class UnspentOutputAccount(
    val keyHash: String,
    val unspentUtxo: UnspentOutput
)

class InputSetter {
    fun setInputs(
        mutableBTCTransaction: MutableBTCTransaction,
        unspentOutputs: List<UnspentOutput>
    ) {
        val inputs = mutableListOf<InputToSign>()
        unspentOutputs.forEach {
            inputs.add(
                InputToSign(
                    BitcoinP2PKHAddress.fromAddress("myPAE9HwPeKHh8FjKwBNBaHnemApo3dw6e"),
                    it.txid,
                    it.vout
                )
            )
        }
        mutableBTCTransaction.inputsToSign.addAll(inputs)
    }
}
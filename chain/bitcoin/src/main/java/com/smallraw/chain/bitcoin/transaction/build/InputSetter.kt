package com.smallraw.chain.bitcoin.transaction.build

import com.smallraw.chain.bitcoin.models.UnspentOutput
import com.smallraw.chain.bitcoin.transaction.DustCalculator
import com.smallraw.chain.bitcoin.unspentOutput.UnspentOutputSelector
import com.smallraw.chain.bitcoincore.script.Script
import com.smallraw.chain.bitcoincore.script.ScriptType

class InputSetter(
    private val unspentOutputSelector: UnspentOutputSelector,
    private val dustCalculator: DustCalculator,
) {
    fun setInputs(
        mutableTransaction: MutableTransaction,
        unspentOutputWiths: List<UnspentOutput>? = null
    ) {
        val inputs = mutableListOf<InputToSign>()
        unspentOutputWiths?.forEach {
            inputs.add(
                InputToSign(
                    it.address,
                    it.txid,
                    it.vout,
                    value = it.value,
                    redeemScript = it.redeemScript?.let { it1 -> Script(it1) }
                )
            )
        }
        mutableTransaction.inputsToSign.addAll(inputs)
    }

    fun setInputs(
        mutableTransaction: MutableTransaction,
        feeRate: Int,
        senderPay: Boolean
    ) {
        val amount = mutableTransaction.recipientValue
        val dust = dustCalculator.dust(ScriptType.P2PKH)
        val unspentOutputInfo = unspentOutputSelector.select(
            amount,
            feeRate,
            mutableTransaction.recipientAddress?.scriptType() ?: ScriptType.UNKNOWN,
            mutableTransaction.changeAddress?.scriptType() ?: ScriptType.UNKNOWN,
            senderPay,
            dust,
        )

        val inputs = ArrayList<InputToSign>(unspentOutputInfo.outputs.size)
        for (unspentOutput in unspentOutputInfo.outputs) {
            inputs.add(inputToSign(unspentOutput))
        }
        mutableTransaction.inputsToSign.addAll(inputs)

        mutableTransaction.recipientValue = unspentOutputInfo.recipientValue

        // Add change output if needed
        unspentOutputInfo.changeValue?.let { changeValue ->
            mutableTransaction.changeValue = changeValue
        }
    }

    private fun inputToSign(unspentOutput: UnspentOutput): InputToSign {
        return InputToSign(unspentOutput.address,
            unspentOutput.txid,
            unspentOutput.vout,
            value = unspentOutput.value,
            scriptPubKeyType = unspentOutput.address.scriptType(),
            isWitness = unspentOutput.address.scriptType().isWitness,
            redeemScript = unspentOutput.redeemScript?.let { it1 -> Script(it1) }
        )
    }
}
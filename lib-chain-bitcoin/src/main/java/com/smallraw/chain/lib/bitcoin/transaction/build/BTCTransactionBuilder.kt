package com.smallraw.chain.lib.bitcoin.transaction.build

import com.smallraw.chain.lib.bitcoin.models.UnspentOutputWithAddress

class BTCTransactionBuilder(
    private val iRecipientSetter: IRecipientSetter,
    private val iChangeSetter: IChangeSetter,
    private val inputSetter: InputSetter,
    private val outputSetter: OutputSetter,
    private val btcTransactionSigner: IBTCTransactionSigner = EmptyBTCTransactionSigner()
) {

    fun build(
        unspentOutputWiths: List<UnspentOutputWithAddress>,
        recipientAddress: String,
        recipientValue: Long = 0L,
        changeAddress: String? = null,
        changeValue: Long = 0L
    ): MutableBTCTransaction {
        val mutableBTCTransaction = MutableBTCTransaction()

        iRecipientSetter.setRecipient(mutableBTCTransaction, recipientAddress, recipientValue)
        changeAddress?.let {
            iChangeSetter.setChange(mutableBTCTransaction, changeAddress, changeValue)
        }
        inputSetter.setInputs(mutableBTCTransaction, unspentOutputWiths)
        outputSetter.setOutputs(mutableBTCTransaction)

        btcTransactionSigner.sign(mutableBTCTransaction)
        return mutableBTCTransaction
    }

    open class BuilderException : Exception() {
        class FeeMoreThanValue : BuilderException()
        class NotSupportedScriptType : BuilderException()
    }
}
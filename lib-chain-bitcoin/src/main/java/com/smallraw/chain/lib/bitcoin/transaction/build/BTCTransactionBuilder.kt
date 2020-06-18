package com.smallraw.chain.lib.bitcoin.transaction.build

import com.smallraw.chain.lib.bitcoin.BitcoinP2PKHAddress
import com.smallraw.chain.lib.bitcoin.models.UnspentOutput

class BTCTransactionBuilder(
    private val IRecipientSetter: IRecipientSetter,
    private val inputSetter: InputSetter,
    private val outputSetter: OutputSetter,
    private val btcTransactionSigner: IBTCTransactionSigner = EmptyBTCTransactionSigner()
) {

    fun build(
        unspentOutputs: List<UnspentOutput>,
        recipientAddress: String,
        recipientValue: Long = 0L,
        changeAddress: String? = null,
        changeValue: Long = 0L
    ): MutableBTCTransaction {
        val mutableBTCTransaction = MutableBTCTransaction()

        //test
        changeAddress?.let {
            mutableBTCTransaction.changeAddress = BitcoinP2PKHAddress.fromAddress(changeAddress)
            mutableBTCTransaction.changeValue = changeValue
        }

        IRecipientSetter.setRecipient(mutableBTCTransaction, recipientAddress, recipientValue)
        inputSetter.setInputs(mutableBTCTransaction, unspentOutputs)
        outputSetter.setOutputs(mutableBTCTransaction)

        btcTransactionSigner.sign(mutableBTCTransaction)
        return mutableBTCTransaction
    }

    open class BuilderException : Exception() {
        class FeeMoreThanValue : BuilderException()
        class NotSupportedScriptType : BuilderException()
    }
}
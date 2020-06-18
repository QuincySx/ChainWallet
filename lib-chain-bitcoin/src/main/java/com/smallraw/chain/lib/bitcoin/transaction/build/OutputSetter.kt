package com.smallraw.chain.lib.bitcoin.transaction.build

class OutputSetter {
    fun setOutputs(
        transaction: MutableBTCTransaction
    ) {
        val list = mutableListOf<TransactionOutput>()
        transaction.recipientAddress.let {
            list.add(
                TransactionOutput(it, transaction.recipientValue)
            )
        }

        transaction.changeAddress?.let {
            list.add(
                TransactionOutput(it, transaction.changeValue)
            )
        }
        transaction.outputs = list
    }
}
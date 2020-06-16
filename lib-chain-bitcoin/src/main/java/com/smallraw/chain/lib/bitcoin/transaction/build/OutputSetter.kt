package com.smallraw.chain.lib.bitcoin.transaction.build

class OutputSetter {
    fun setOutputs(
        transaction: MutableBTCTransaction
    ) {
        val list = mutableListOf<TransactionOutput>()
        transaction.recipientAddress.let {
            list.add(
                TransactionOutput(
                    transaction.recipientValue,
                    0,
                    it.lockingScript,
                    it.scriptType,
                    it.hashKey
                )
            )
        }

        transaction.changeAddress?.let {
            list.add(
                TransactionOutput(
                    transaction.changeValue,
                    0,
                    it.lockingScript,
                    it.scriptType,
                    it.hashKey
                )
            )
        }
        transaction.outputs = list
    }
}
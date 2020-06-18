package com.smallraw.chain.lib.bitcoin.transaction.build

import com.smallraw.chain.lib.bitcoin.BitcoinAddress
import com.smallraw.chain.lib.bitcoin.BitcoinP2PKHAddress

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
                    transaction.recipientAddress
                )
            )
        }

        transaction.changeAddress?.let {
            list.add(
                TransactionOutput(
                    transaction.changeValue,
                    0,
                    it.lockingScript,
                    transaction.changeAddress
                )
            )
        }
        transaction.outputs = list
    }
}
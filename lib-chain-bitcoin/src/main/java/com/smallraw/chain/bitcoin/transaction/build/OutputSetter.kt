package com.smallraw.chain.bitcoin.transaction.build

/**
 * 将 recipientAddress 和 changeAddress 放入 output 列表
 * 可以继续添加其他 output 到交易中
 */
class OutputSetter {
    fun setOutputs(
        outputs: List<TransactionOutput>,
        transaction: MutableTransaction
    ) {
        val list = mutableListOf<TransactionOutput>()
        transaction.recipientAddress?.let {
            list.add(
                TransactionOutput(it, transaction.recipientValue)
            )
        }

        transaction.changeAddress?.let {
            list.add(
                TransactionOutput(it, transaction.changeValue)
            )
        }
        list.addAll(outputs)
        transaction.outputs = list
    }
}
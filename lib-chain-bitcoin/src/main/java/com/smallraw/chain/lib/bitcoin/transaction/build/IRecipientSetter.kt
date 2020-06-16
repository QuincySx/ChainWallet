package com.smallraw.chain.lib.bitcoin.transaction.build

import com.smallraw.chain.lib.bitcoin.BitcoinP2PKHAddress

interface IRecipientSetter {
    fun setRecipient(mutableTransaction: MutableBTCTransaction, toAddress: String, value: Long)
}

class RecipientSetter : IRecipientSetter {
    override fun setRecipient(
        mutableTransaction: MutableBTCTransaction,
        toAddress: String,
        value: Long
    ) {
        // todo Address Convert
        mutableTransaction.recipientAddress = BitcoinP2PKHAddress.fromAddress(toAddress)
        mutableTransaction.recipientValue = value
    }
}
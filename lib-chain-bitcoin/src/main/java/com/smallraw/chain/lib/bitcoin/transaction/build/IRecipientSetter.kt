package com.smallraw.chain.lib.bitcoin.transaction.build

import com.smallraw.chain.lib.bitcoin.BitcoinP2PKHAddress
import com.smallraw.chain.lib.bitcoin.convert.AddressConverterChain

interface IRecipientSetter {
    fun setRecipient(mutableTransaction: MutableBTCTransaction, toAddress: String, value: Long)
}

class RecipientSetter(private val addressConverterChain: AddressConverterChain) : IRecipientSetter {
    override fun setRecipient(
        mutableTransaction: MutableBTCTransaction,
        toAddress: String,
        value: Long
    ) {
        // todo Address Convert
        mutableTransaction.recipientAddress = addressConverterChain.convert(toAddress)
        mutableTransaction.recipientValue = value
    }
}
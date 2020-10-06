package com.smallraw.chain.bitcoin.transaction.build

import com.smallraw.chain.bitcoin.convert.AddressConverterChain

/**
 * 设置交易的接收地址
 */
interface IRecipientSetter {
    fun setRecipient(mutableTransaction: MutableTransaction, toAddress: String, value: Long)
}

class RecipientSetter(private val addressConverterChain: AddressConverterChain) : IRecipientSetter {
    override fun setRecipient(
        mutableTransaction: MutableTransaction,
        toAddress: String,
        value: Long
    ) {
        mutableTransaction.recipientAddress = addressConverterChain.convert(toAddress)
        mutableTransaction.recipientValue = value
    }
}
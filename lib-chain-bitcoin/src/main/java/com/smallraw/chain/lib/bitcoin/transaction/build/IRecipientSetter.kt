package com.smallraw.chain.lib.bitcoin.transaction.build

import com.smallraw.chain.lib.bitcoin.convert.AddressConverterChain

/**
 * 设置交易的接收地址
 */
interface IRecipientSetter {
    fun setRecipient(mutableTransaction: MutableBTCTransaction, toAddress: String, value: Long)
}

class RecipientSetter(private val addressConverterChain: AddressConverterChain) : IRecipientSetter {
    override fun setRecipient(
        mutableTransaction: MutableBTCTransaction,
        toAddress: String,
        value: Long
    ) {
        mutableTransaction.recipientAddress = addressConverterChain.convert(toAddress)
        mutableTransaction.recipientValue = value
    }
}
package com.smallraw.chain.bitcoin.transaction.build.`interface`

import com.smallraw.chain.bitcoin.transaction.build.MutableTransaction
import com.smallraw.chain.bitcoincore.addressConvert.AddressConverter

/**
 * 设置交易的接收地址
 */
interface IRecipientSetter {
    fun setRecipient(mutableTransaction: MutableTransaction, toAddress: String, value: Long)
}

class RecipientSetter(private val addressConverterChain: AddressConverter) : IRecipientSetter {
    override fun setRecipient(
        mutableTransaction: MutableTransaction,
        toAddress: String,
        value: Long
    ) {
        mutableTransaction.recipientAddress = addressConverterChain.convert(toAddress)
        mutableTransaction.recipientValue = value
    }
}
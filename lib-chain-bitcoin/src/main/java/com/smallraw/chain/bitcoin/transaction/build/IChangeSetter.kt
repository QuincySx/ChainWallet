package com.smallraw.chain.bitcoin.transaction.build

import com.smallraw.chain.bitcoin.convert.AddressConverterChain

/**
 * 设置交易的找零地址
 */
interface IChangeSetter {
    fun setChange(mutableTransaction: MutableBTCTransaction, toAddress: String, value: Long)
}

class ChangeSetter(private val addressConverterChain: AddressConverterChain) : IChangeSetter {
    override fun setChange(
        mutableTransaction: MutableBTCTransaction,
        toAddress: String,
        value: Long
    ) {
        mutableTransaction.changeAddress = addressConverterChain.convert(toAddress)
        mutableTransaction.changeValue = value
    }
}
package com.smallraw.chain.bitcoin.transaction.build.`interface`

import com.smallraw.chain.bitcoin.convert.AddressConverterChain
import com.smallraw.chain.bitcoin.transaction.build.MutableTransaction

/**
 * 设置交易的找零地址
 */
interface IChangeSetter {
    fun setChange(mutableTransaction: MutableTransaction, toAddress: String, value: Long)
}

class ChangeSetter(private val addressConverterChain: AddressConverterChain) : IChangeSetter {
    override fun setChange(
        mutableTransaction: MutableTransaction,
        toAddress: String,
        value: Long
    ) {
        mutableTransaction.changeAddress = addressConverterChain.convert(toAddress)
        mutableTransaction.changeValue = value
    }
}
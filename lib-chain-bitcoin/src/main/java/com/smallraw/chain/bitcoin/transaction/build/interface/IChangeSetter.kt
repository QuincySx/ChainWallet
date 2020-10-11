package com.smallraw.chain.bitcoin.transaction.build.`interface`

import com.smallraw.chain.bitcoin.transaction.build.MutableTransaction
import com.smallraw.chain.bitcoincore.addressConvert.AddressConverter

/**
 * 设置交易的找零地址
 */
interface IChangeSetter {
    fun setChange(mutableTransaction: MutableTransaction, toAddress: String, value: Long)
}

class ChangeSetter(private val addressConverterChain: AddressConverter) : IChangeSetter {
    override fun setChange(
        mutableTransaction: MutableTransaction,
        toAddress: String,
        value: Long
    ) {
        mutableTransaction.changeAddress = addressConverterChain.convert(toAddress)
        mutableTransaction.changeValue = value
    }
}
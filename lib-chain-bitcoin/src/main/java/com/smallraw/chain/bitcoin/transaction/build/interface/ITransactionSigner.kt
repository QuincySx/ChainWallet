package com.smallraw.chain.bitcoin.transaction.build.`interface`

import com.smallraw.chain.bitcoin.transaction.build.MutableTransaction

interface ITransactionSigner {
    fun sign(mutableTransaction: MutableTransaction)
}
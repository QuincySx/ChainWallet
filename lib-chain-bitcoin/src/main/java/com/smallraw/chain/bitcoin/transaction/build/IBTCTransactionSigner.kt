package com.smallraw.chain.bitcoin.transaction.build

interface IBTCTransactionSigner {
    fun sign(mutableBTCTransaction: MutableBTCTransaction)
}
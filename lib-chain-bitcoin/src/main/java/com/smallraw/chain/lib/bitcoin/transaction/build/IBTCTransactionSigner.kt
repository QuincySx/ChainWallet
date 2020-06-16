package com.smallraw.chain.lib.bitcoin.transaction.build

interface IBTCTransactionSigner {
    fun sign(mutableBTCTransaction: MutableBTCTransaction)
}
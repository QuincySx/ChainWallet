package com.smallraw.chain.bitcoin.transaction.build

class EmptyBTCTransactionSigner : IBTCTransactionSigner {
    override fun sign(mutableTransaction: MutableTransaction) {}
}
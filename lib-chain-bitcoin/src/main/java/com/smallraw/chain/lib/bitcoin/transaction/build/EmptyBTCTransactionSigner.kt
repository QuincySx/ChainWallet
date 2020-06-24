package com.smallraw.chain.lib.bitcoin.transaction.build

class EmptyBTCTransactionSigner : IBTCTransactionSigner {
    override fun sign(mutableBTCTransaction: MutableBTCTransaction) {}
}
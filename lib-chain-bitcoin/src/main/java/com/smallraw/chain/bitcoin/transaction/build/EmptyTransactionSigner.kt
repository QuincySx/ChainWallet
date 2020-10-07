package com.smallraw.chain.bitcoin.transaction.build

import com.smallraw.chain.bitcoin.transaction.build.`interface`.ITransactionSigner

class EmptyTransactionSigner : ITransactionSigner {
    override fun sign(mutableTransaction: MutableTransaction) {}
}
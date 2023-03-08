package com.smallraw.chain.wallet.data.bean

data class Account(
    var id: Long? = null,
    var walletId: Long? = null,
    var name: String,
    private var derivedPath: String = "",
    var address: String
)

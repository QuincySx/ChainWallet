package com.smallraw.chain.wallet.data.bean

import com.smallraw.chain.wallet.data.database.entity.AccountDO

data class Account(
    var id: Long? = null,
    var name: String,
    var encrypted: String,
    @AccountDO.AccountType
    var accountType: Int = AccountDO.AccountType.MNEMONIC,
    @AccountDO.SourceType
    var sourceType: Int = AccountDO.SourceType.CREATE,
)

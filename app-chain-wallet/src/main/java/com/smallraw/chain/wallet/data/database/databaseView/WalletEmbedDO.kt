package com.smallraw.chain.wallet.data.database.databaseView

import androidx.room.Embedded
import androidx.room.Relation
import com.smallraw.chain.wallet.data.database.entity.AccountDO
import com.smallraw.chain.wallet.data.database.entity.WalletDO

data class WalletEmbedDO(
    @Embedded
    val account: WalletDO,

    @Relation(
        parentColumn = "wallet_id",
        entityColumn = "id"
    )
    val accounts: AccountDO,
)
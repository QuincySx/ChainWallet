package com.smallraw.chain.wallet.data.database.databaseView

import androidx.room.Embedded
import androidx.room.Relation
import com.smallraw.chain.wallet.data.database.entity.AccountDO
import com.smallraw.chain.wallet.data.database.entity.ChainDO
import com.smallraw.chain.wallet.data.database.entity.WalletDO

data class WalletEmbedDO(
    @Embedded
    val wallet: WalletDO,

    @Relation(
        parentColumn = "account_id",
        entityColumn = "id"
    )
    val account: AccountDO,

    @Relation(
        parentColumn = "chain_table_id",
        entityColumn = "id"
    )
    val chain: ChainDO
)
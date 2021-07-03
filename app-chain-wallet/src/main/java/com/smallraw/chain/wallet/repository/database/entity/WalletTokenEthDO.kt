package com.smallraw.chain.wallet.repository.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = WalletTokenEthDO.TABLE_NAME,
    indices = [
        Index(value = ["wallet_id", "token_id"], unique = true)
    ]
)
class WalletTokenEthDO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null,

    @ColumnInfo(name = "wallet_id")
    var walletId: Long,

    @ColumnInfo(name = "token_id")
    var tokenId: Long,

    @ColumnInfo(name = "balance")
    var balance: Long = 0,
) {
    companion object {
        const val TABLE_NAME = "wallet_token_eth_table"
    }
}
package com.smallraw.chain.wallet.data.database.entity.coinEth

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.smallraw.chain.wallet.data.database.entity.embed.BaseEntity

@Entity(
    tableName = WalletTokenEthDO.TABLE_NAME,
    indices = [
        Index(value = ["wallet_id", "token_id"], unique = true)
    ]
)
data class WalletTokenEthDO(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    override val id: Long? = null,

    @ColumnInfo(name = "wallet_id")
    var walletId: Long,

    @ColumnInfo(name = "token_id")
    var tokenId: Long,

    @ColumnInfo(name = "balance")
    var balance: Long = 0,
) : BaseEntity(id) {
    companion object {
        const val TABLE_NAME = "wallet_token_eth_table"
    }
}
package com.smallraw.chain.wallet.data.database.entity.coinEth

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.smallraw.chain.wallet.data.database.entity.embed.BaseEntity

@Entity(
    tableName = TokenEthDO.TABLE_NAME,
    indices = [
        Index(value = ["chain_table_id", "address"], unique = true)
    ]
)
data class TokenEthDO(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    override val id: Long? = null,

    @ColumnInfo(name = "chain_table_id")
    var chainTableId: Long,

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "decimals")
    var decimals: String = "",

    @ColumnInfo(name = "symbol")
    var symbol: String = "",

    @ColumnInfo(name = "address")
    var address: String = "",

    @ColumnInfo(name = "logoURI")
    var logoURI: String = "",

    @ColumnInfo(name = "tags")
    var tags: String = "",
): BaseEntity(id) {
    companion object {
        const val TABLE_NAME = "token_eth_table"
    }
}
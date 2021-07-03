package com.smallraw.chain.wallet.repository.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = TokenEthDO.TABLE_NAME,
    indices = [
        Index(value = ["chain_table_id", "address"], unique = true)
    ]
)
class TokenEthDO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null,

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
) {
    companion object {
        const val TABLE_NAME = "token_eth_table"
    }
}
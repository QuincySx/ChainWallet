package com.smallraw.chain.wallet.repository.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.smallraw.chain.wallet.App

@Entity(
    tableName = ChainRpcURLDO.TABLE_NAME,
    indices = [
        Index(value = ["chain_table_id", "chain_type"], unique = true)
    ]
)
class ChainRpcURLDO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null,

    @ColumnInfo(name = "chain_table_id")
    var chainTableId: Long,

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "chain_type")
    var chainType: App.ChainType = App.ChainType.ETH,

    @ColumnInfo(name = "url")
    var url: String = "",

    @ColumnInfo(name = "port")
    var port: String = "",

    ) {
    companion object {
        const val TABLE_NAME = "chain_rpc_url_table"
    }
}
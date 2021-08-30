package com.smallraw.chain.wallet.data.database.entity

import androidx.room.*
import com.smallraw.chain.wallet.App
import com.smallraw.chain.wallet.data.database.entity.embed.BaseEntity
import com.smallraw.chain.wallet.data.database.entity.embed.DigitalAssetUnitDO

@Entity(
    tableName = ChainDO.TABLE_NAME,
    indices = [
        Index(value = ["bip_44_index", "chain_id"], unique = true)
    ]
)
data class ChainDO(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    override val id: Long? = null,

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "chain_type")
    var chainType: App.ChainType = App.ChainType.BTC,

    @ColumnInfo(name = "chain_id")
    var chainId: Long,

    @ColumnInfo(name = "bip_44_index")
    var bip44Index: Int,

    @Embedded
    val currency: DigitalAssetUnitDO,

    @ColumnInfo(name = "other_payload")
    var other: String,
) : BaseEntity(id) {
    companion object {
        const val TABLE_NAME = "chain_table"
    }
}
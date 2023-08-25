package com.smallraw.core.chain.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

enum class WalletType {
    HD, // HD钱包
    WATCH, // 观察钱包
    IMPORT, // 导入钱包
}

@Entity(
    tableName = "wallets",
    foreignKeys = [ForeignKey(
        entity = CurrencyEntity::class,
        parentColumns = ["currencyId"],
        childColumns = ["currencyId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class WalletEntity(
    @PrimaryKey(autoGenerate = true) val walletId: Long = 0,
    val currencyId: Long,
    val walletName: String,
    val walletType: WalletType,
    val xpub: String,
    val addressIndex: Int,
    val createdAt: Long
)

class WalletTypeConverter {
    @androidx.room.TypeConverter
    fun stringToWalletType(value: String?): WalletType? =
        WalletType.values().find { it.name == value }

    @androidx.room.TypeConverter
    fun walletTypeToString(walletType: WalletType?): String? =
        walletType?.name
}
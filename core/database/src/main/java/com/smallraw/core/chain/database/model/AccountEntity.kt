package com.smallraw.core.chain.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

enum class AccountType(val value: String) {
    /**
     * 普通账户
     */
    Normal("normal"),

    /**
     * 多签账户
     */
    MultiSign("multiSign"),

    /**
     * UTXO 账户
     */
    UTXO("utxo")
}

@Entity(
    tableName = "accounts",
    foreignKeys = [ForeignKey(
        entity = WalletEntity::class,
        parentColumns = ["walletId"],
        childColumns = ["walletId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val accountId: Long = 0,
    val walletId: Long,
    val accountName: String,
    val accountType: AccountType,
    val accountData: String
)

class AccountTypeConverter {
    @TypeConverter
    fun stringToAccountType(value: String?): AccountType? =
        AccountType.values().find { it.value == value }

    @TypeConverter
    fun accountTypeToString(accountType: AccountType?): String? =
        accountType?.value
}

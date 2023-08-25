package com.smallraw.core.chain.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencies")
data class CurrencyEntity(
    @PrimaryKey(autoGenerate = true) val currencyId: Long = 0,
    val ticker: String,
    val name: String,
    val bip32Version: String,
    val bip44CoinType: Int
)
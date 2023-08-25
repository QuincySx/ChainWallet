package com.smallraw.core.chain.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = WalletEntity::class,
            parentColumns = ["walletId"],
            childColumns = ["walletId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CurrencyEntity::class,
            parentColumns = ["currencyId"],
            childColumns = ["currencyId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val transactionId: Long = 0,
    val walletId: Long,
    val currencyId: Long,
    val txHash: String,
    val fromAddress: String,
    val toAddress: String,
    val amount: Double,
    val fee: Double,
    val status: String,
    val timestamp: Long
)

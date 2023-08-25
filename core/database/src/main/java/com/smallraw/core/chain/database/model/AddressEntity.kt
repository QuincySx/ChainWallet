package com.smallraw.core.chain.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.math.BigInteger

@Entity(
    tableName = "addresses",
    foreignKeys = [ForeignKey(
        entity = WalletEntity::class,
        parentColumns = ["walletId"],
        childColumns = ["walletId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class AddressEntity(
    @PrimaryKey(autoGenerate = true) val addressId: Long = 0,
    val walletId: Long,
    val address: String,
    val addressIndex: Int,
    val balance: BigInteger
)
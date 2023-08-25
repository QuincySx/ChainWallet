package com.smallraw.core.chain.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

enum class SecretType {
    Mnemonic,
    PrivateKey,
    WIF
}


@Entity(tableName = "encrypted_secrets",
    foreignKeys = [ForeignKey(
        entity = WalletEntity::class,
        parentColumns = ["walletId"],
        childColumns = ["walletId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class EncryptedSecretEntity(
    @PrimaryKey(autoGenerate = true) val secretId: Long = 0,
    val walletId: Long,
    val secretType: SecretType,
    val secretData: String
)

class SecretTypeConverter {
    @androidx.room.TypeConverter
    fun stringToSecretType(value: String?): SecretType? =
        SecretType.values().find { it.name == value }

    @androidx.room.TypeConverter
    fun secretTypeToString(secretType: SecretType?): String? =
        secretType?.name
}

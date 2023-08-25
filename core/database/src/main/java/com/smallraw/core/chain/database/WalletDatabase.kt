package com.smallraw.core.chain.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.smallraw.core.chain.database.dao.AccountDao
import com.smallraw.core.chain.database.dao.AddressDao
import com.smallraw.core.chain.database.dao.CurrencyDao
import com.smallraw.core.chain.database.dao.EncryptedSecretDao
import com.smallraw.core.chain.database.dao.TransactionDao
import com.smallraw.core.chain.database.dao.WalletDao
import com.smallraw.core.chain.database.model.AccountEntity
import com.smallraw.core.chain.database.model.AccountTypeConverter
import com.smallraw.core.chain.database.model.AddressEntity
import com.smallraw.core.chain.database.model.CurrencyEntity
import com.smallraw.core.chain.database.model.EncryptedSecretEntity
import com.smallraw.core.chain.database.model.SecretTypeConverter
import com.smallraw.core.chain.database.model.TransactionEntity
import com.smallraw.core.chain.database.model.WalletEntity
import com.smallraw.core.chain.database.model.WalletTypeConverter
import com.smallraw.core.chain.database.util.BigIntegerConverter
import com.smallraw.core.chain.database.util.ByteArrayConverter
import com.smallraw.core.chain.database.util.InstantConverter

@Database(
    entities = [
        CurrencyEntity::class,
        WalletEntity::class,
        AddressEntity::class,
        TransactionEntity::class,
        EncryptedSecretEntity::class,
        AccountEntity::class,
    ],
    version = 0,
    autoMigrations = [],
    exportSchema = true,
)
@TypeConverters(
    InstantConverter::class,
    ByteArrayConverter::class,
    BigIntegerConverter::class,
    WalletTypeConverter::class,
    AccountTypeConverter::class,
    SecretTypeConverter::class,
)
abstract class WalletDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
    abstract fun walletDao(): WalletDao
    abstract fun addressDao(): AddressDao
    abstract fun transactionDao(): TransactionDao
    abstract fun encryptedSecretDao(): EncryptedSecretDao
    abstract fun accountDao(): AccountDao
}
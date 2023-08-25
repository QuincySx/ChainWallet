package com.smallraw.core.chain.database

import com.smallraw.core.chain.database.dao.AccountDao
import com.smallraw.core.chain.database.dao.AddressDao
import com.smallraw.core.chain.database.dao.CurrencyDao
import com.smallraw.core.chain.database.dao.EncryptedSecretDao
import com.smallraw.core.chain.database.dao.TransactionDao
import com.smallraw.core.chain.database.dao.WalletDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    fun providesAccountDao(
        database: WalletDatabase,
    ): AccountDao = database.accountDao()

    @Provides
    fun providesWalletDao(
        database: WalletDatabase,
    ): WalletDao = database.walletDao()

    @Provides
    fun providesAddressDao(
        database: WalletDatabase,
    ): AddressDao = database.addressDao()

    @Provides
    fun providesTransactionDao(
        database: WalletDatabase,
    ): TransactionDao = database.transactionDao()

    @Provides
    fun providesEncryptedSecretDao(
        database: WalletDatabase,
    ): EncryptedSecretDao = database.encryptedSecretDao()

    @Provides
    fun providesCurrencyDao(
        database: WalletDatabase,
    ): CurrencyDao = database.currencyDao()

}

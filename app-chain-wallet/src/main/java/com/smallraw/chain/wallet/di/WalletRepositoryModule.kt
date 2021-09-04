package com.smallraw.chain.wallet.di

import com.smallraw.chain.wallet.data.IWalletRepository
import com.smallraw.chain.wallet.data.database.AppDatabase
import com.smallraw.chain.wallet.data.database.dataSource.IWalletDataBaseDataSource
import com.smallraw.chain.wallet.data.database.dataSource.impl.WalletLocalDataSource
import com.smallraw.chain.wallet.data.repository.WalletRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WalletRepositoryModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class WalletDataBaseDataSource

    @Singleton
    @WalletDataBaseDataSource
    @Provides
    fun provideWalletDataBaseDataSource(
        database: AppDatabase,
        @CoroutineDispatcherModule.DefaultDispatcher ioDispatcher: CoroutineDispatcher
    ): IWalletDataBaseDataSource {
        return WalletLocalDataSource(
            database.walletDao(),
            database.accountDao(),
            ioDispatcher
        )
    }

    @Singleton
    @Provides
    fun provideWalletRepository(
        @WalletDataBaseDataSource walletDataBaseDataSource: IWalletDataBaseDataSource,
        @CoroutineDispatcherModule.IODispatcher ioDispatcher: CoroutineDispatcher
    ): IWalletRepository {
        return WalletRepository(
            walletDataBaseDataSource, ioDispatcher
        )
    }
}
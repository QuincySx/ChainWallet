package com.smallraw.chain.wallet.di

import android.content.Context
import androidx.room.Room
import com.smallraw.chain.wallet.data.IWalletRepository
import com.smallraw.chain.wallet.data.database.AppDatabase
import com.smallraw.chain.wallet.data.database.dataSource.IWalletDataBaseDataSource
import com.smallraw.chain.wallet.data.database.dataSource.impl.WalletLocalDataSource
import com.smallraw.chain.wallet.data.repository.WalletRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.annotation.AnnotationRetention.RUNTIME

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Qualifier
    @Retention(RUNTIME)
    annotation class RemoteTasksDataSource

    @Qualifier
    @Retention(RUNTIME)
    annotation class WalletDataBaseDataSource

    @Singleton
    @WalletDataBaseDataSource
    @Provides
    fun provideTasksLocalDataSource(
        database: AppDatabase,
        @CoroutineDispatcherModule.DefaultDispatcher ioDispatcher: CoroutineDispatcher
    ): IWalletDataBaseDataSource {
        return WalletLocalDataSource(
            database.walletDao(),
            ioDispatcher
        )
    }

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object WalletRepositoryModule {

    @Singleton
    @Provides
    fun provideWalletRepository(
        @AppModule.WalletDataBaseDataSource walletDataBaseDataSource: IWalletDataBaseDataSource,
        @CoroutineDispatcherModule.IODispatcher ioDispatcher: CoroutineDispatcher
    ): IWalletRepository {
        return WalletRepository(
            walletDataBaseDataSource, ioDispatcher
        )
    }
}
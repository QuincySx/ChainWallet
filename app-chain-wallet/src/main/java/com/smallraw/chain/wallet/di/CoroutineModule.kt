package com.smallraw.chain.wallet.di

import com.smallraw.chain.wallet.di.CoroutineDispatcherModule.DefaultDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.annotation.AnnotationRetention.RUNTIME

@Module
@InstallIn(SingletonComponent::class)
object CoroutineDispatcherModule {
    @Retention(RUNTIME)
    @Qualifier
    annotation class DefaultDispatcher

    @Retention(RUNTIME)
    @Qualifier
    annotation class IODispatcher

    @Retention(RUNTIME)
    @Qualifier
    annotation class MainDispatcher

    @Retention(RUNTIME)
    @Qualifier
    annotation class MainImmediateDispatcher

    @Singleton
    @Provides
    fun providesDefaultScopeDispatcher() = Dispatchers.IO

    @DefaultDispatcher
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @IODispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @MainImmediateDispatcher
    @Provides
    fun providesMainImmediateDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate
}

@InstallIn(SingletonComponent::class)
@Module
object CoroutinesScopesModule {

    @ApplicationScope
    @Singleton
    @Provides
    fun providesAppCoroutineScope(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)
}
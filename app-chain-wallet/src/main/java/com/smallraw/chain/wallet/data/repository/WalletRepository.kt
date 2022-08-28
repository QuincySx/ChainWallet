package com.smallraw.chain.wallet.data.repository

import com.smallraw.chain.wallet.data.IWalletRepository
import com.smallraw.chain.wallet.data.bean.Wallet
import com.smallraw.chain.wallet.data.database.dataSource.IWalletDataBaseDataSource
import com.smallraw.chain.wallet.data.database.entity.WalletDO
import com.smallraw.chain.wallet.feature.wallets.bean.WalletAccountListBean
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class WalletRepository(
    private val walletLocalDataSource: IWalletDataBaseDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IWalletRepository {

    override suspend fun getWalletCount(): Int {
        return withContext(ioDispatcher) {
            walletLocalDataSource.getWalletCount()
        }
    }

    override suspend fun existsWallet(): Flow<Boolean> {
        return walletLocalDataSource.getWalletCountStream().map { it > 0 }
    }

    override suspend fun getAccountWallet(): Flow<List<WalletAccountListBean>> {
        return withContext(ioDispatcher) {
            walletLocalDataSource.getWalletAccount()
        }
    }

    private suspend fun createWallet(
        name: String,
        password: String,
        secret: String? = null,
        @WalletDO.Type type: Int = WalletDO.Type.MNEMONIC,
        @WalletDO.SourceType sourceType: Int = secret?.let
        { WalletDO.SourceType.IMPORT }
            ?: WalletDO.SourceType.CREATE
    ): Wallet {
        return withContext(ioDispatcher) {
            walletLocalDataSource.createWallet(name, sourceType, type, secret ?: "")
        }
    }

    override suspend fun createWallet(
        name: String,
        password: String
    ): Wallet {
        return withContext(ioDispatcher) {
            createWallet(name, password, null, WalletDO.Type.MNEMONIC, WalletDO.SourceType.CREATE)
        }
    }

    override suspend fun importWalletByPrivateKey(
        name: String,
        password: String,
        privateKey: String
    ): Wallet {
        return createWallet(
            name,
            password,
            privateKey,
            WalletDO.Type.PRIVATE,
            WalletDO.SourceType.IMPORT
        )
    }

    override suspend fun importWalletByMnemonic(
        name: String,
        password: String,
        mnemonic: String
    ): Wallet {
        return createWallet(
            name,
            password,
            mnemonic,
            WalletDO.Type.MNEMONIC,
            WalletDO.SourceType.IMPORT
        )
    }

    override suspend fun importWalletByKeystore(
        name: String,
        password: String,
        keystore: String
    ): Wallet {
        return createWallet(
            name,
            password,
            keystore,
            WalletDO.Type.KEYSTORE,
            WalletDO.SourceType.IMPORT
        )
    }

    override suspend fun deleteWallet(wallet: Wallet): Boolean {
        TODO("Not yet implemented")
    }

}
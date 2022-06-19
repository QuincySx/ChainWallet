package com.smallraw.chain.wallet.data.repository

import com.smallraw.chain.wallet.data.IWalletRepository
import com.smallraw.chain.wallet.data.database.dataSource.IWalletDataBaseDataSource
import com.smallraw.chain.wallet.feature.wallets.bean.WalletAccountListBean
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class WalletRepository(
    private val walletLocalDataSource: IWalletDataBaseDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IWalletRepository {

    override suspend fun getAccountWallet(): Flow<List<WalletAccountListBean>> {
        return withContext(ioDispatcher) {
            walletLocalDataSource.getWalletAccount()
        }
    }

}
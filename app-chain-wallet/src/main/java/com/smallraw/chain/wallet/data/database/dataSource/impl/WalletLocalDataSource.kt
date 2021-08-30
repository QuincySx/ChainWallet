package com.smallraw.chain.wallet.data.database.dataSource.impl

import com.smallraw.chain.wallet.data.bean.IChain
import com.smallraw.chain.wallet.data.database.dao.WalletDao
import com.smallraw.chain.wallet.data.database.dataSource.IWalletDataBaseDataSource
import com.smallraw.chain.wallet.feature.wallets.bean.AccountWalletListBean
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class WalletLocalDataSource internal constructor(
    private val walletDao: WalletDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IWalletDataBaseDataSource {
    override suspend fun getAccountWallet(chain: IChain): Flow<List<AccountWalletListBean>> {
        val chainId = chain.getId() ?: return emptyFlow()
        return flowOf()
    }

}
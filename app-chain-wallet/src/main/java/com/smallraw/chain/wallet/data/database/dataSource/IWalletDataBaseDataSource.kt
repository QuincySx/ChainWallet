package com.smallraw.chain.wallet.data.database.dataSource

import com.smallraw.chain.wallet.data.bean.IChain
import com.smallraw.chain.wallet.feature.wallets.bean.AccountWalletListBean
import kotlinx.coroutines.flow.Flow

interface IWalletDataBaseDataSource {
    suspend fun getAccountWallet(chain: IChain): Flow<List<AccountWalletListBean>>
}
package com.smallraw.chain.wallet.data.database.dataSource

import com.smallraw.chain.wallet.feature.wallets.bean.WalletAccountListBean
import kotlinx.coroutines.flow.Flow

interface IWalletDataBaseDataSource {
    suspend fun getWalletAccount(): Flow<List<WalletAccountListBean>>
}
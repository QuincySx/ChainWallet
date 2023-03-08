package com.smallraw.chain.wallet.data.database.dataSource

import com.smallraw.chain.wallet.data.bean.Wallet
import com.smallraw.chain.wallet.data.database.entity.WalletDO
import com.smallraw.chain.wallet.feature.wallets.bean.WalletAccountListBean
import kotlinx.coroutines.flow.Flow

interface IWalletDataBaseDataSource {
    suspend fun getWalletCount(): Int
    suspend fun getWalletCountStream(): Flow<Int>

    suspend fun getWalletAccount(): Flow<List<WalletAccountListBean>>

    suspend fun createWallet(
        name: String,
        @WalletDO.SourceType sourceType: Int,
        @WalletDO.Type type: Int,
        encrypted: String,
    ): Wallet
}
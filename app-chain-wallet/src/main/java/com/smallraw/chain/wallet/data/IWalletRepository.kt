package com.smallraw.chain.wallet.data

import com.smallraw.chain.wallet.feature.wallets.bean.WalletAccountListBean
import kotlinx.coroutines.flow.Flow

interface IWalletRepository {
    suspend fun getWalletCount(): Flow<Int>
    suspend fun getAccountWallet(): Flow<List<WalletAccountListBean>>
}
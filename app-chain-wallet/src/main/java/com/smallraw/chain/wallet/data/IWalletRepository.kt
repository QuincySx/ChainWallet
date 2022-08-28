package com.smallraw.chain.wallet.data

import com.smallraw.chain.wallet.data.bean.Wallet
import com.smallraw.chain.wallet.feature.wallets.bean.WalletAccountListBean
import kotlinx.coroutines.flow.Flow

interface IWalletRepository {
    suspend fun getWalletCount(): Int
    suspend fun existsWallet(): Flow<Boolean>
    suspend fun getAccountWallet(): Flow<List<WalletAccountListBean>>

    suspend fun createWallet(
        name: String,
        password: String,
    ): Wallet

    suspend fun importWalletByPrivateKey(
        name: String, password: String, privateKey: String
    ): Wallet

    suspend fun importWalletByMnemonic(
        name: String, password: String, mnemonic: String
    ): Wallet

    suspend fun importWalletByKeystore(
        name: String, password: String, keystore: String
    ): Wallet

    suspend fun deleteWallet(wallet: Wallet): Boolean
}
package com.smallraw.chain.wallet.data.database.dataSource.impl

import com.smallraw.chain.wallet.data.bean.Account
import com.smallraw.chain.wallet.data.bean.Wallet
import com.smallraw.chain.wallet.data.database.dao.AccountDao
import com.smallraw.chain.wallet.data.database.dao.WalletDao
import com.smallraw.chain.wallet.data.database.dataSource.IWalletDataBaseDataSource
import com.smallraw.chain.wallet.data.database.entity.AccountDO
import com.smallraw.chain.wallet.data.database.entity.WalletDO
import com.smallraw.chain.wallet.feature.wallets.bean.WalletAccountListBean
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class WalletLocalDataSource internal constructor(
    private val walletDao: WalletDao,
    private val accountDao: AccountDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IWalletDataBaseDataSource {

    private fun convert(walletMap: Map<WalletDO, List<AccountDO>>): List<WalletAccountListBean> {
        return walletMap.map { (wallet, doAccounts) ->

            val accounts = doAccounts.map { account ->
                Account(
                    id = account.id,
                    walletId = account.walletId,
                    name = account.name,
                    address = account.address,
                    derivedPath = account.derivedPath,
                )
            }

            WalletAccountListBean(
                Wallet(
                    id = wallet.id,
                    name = wallet.name,
                    type = wallet.type,
                    isBackup = wallet.isBackup,
                    encrypted = wallet.encrypted,
                    sourceType = wallet.sourceType,
                    createTime = wallet.createdAt,
                ), accounts
            )
        }
    }

    override suspend fun getWalletAccount(): Flow<List<WalletAccountListBean>> {
        val walletMap = walletDao.getAllAndAccount()
        return flowOf(convert(walletMap))
    }
}
package com.smallraw.chain.wallet.data.database.dataSource.impl

import com.smallraw.chain.wallet.App
import com.smallraw.chain.wallet.data.bean.*
import com.smallraw.chain.wallet.data.database.dao.AccountDao
import com.smallraw.chain.wallet.data.database.dao.WalletDao
import com.smallraw.chain.wallet.data.database.dataSource.IWalletDataBaseDataSource
import com.smallraw.chain.wallet.data.database.databaseView.WalletEmbedDO
import com.smallraw.chain.wallet.feature.wallets.bean.AccountWalletListBean
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf

class WalletLocalDataSource internal constructor(
    private val walletDao: WalletDao,
    private val accountDao: AccountDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IWalletDataBaseDataSource {

    private fun convert(wallet: WalletEmbedDO): IWallet {
        val account = Account(
            wallet.account.id,
            wallet.account.name,
            wallet.account.encrypted,
            wallet.account.accountType,
            wallet.account.sourceType
        )
        return when (wallet.chain.chainType) {
            App.ChainType.BTC -> {
                val bitcoinChain = BitcoinChain(
                    wallet.chain.id,
                    wallet.chain.name,
                    wallet.chain.chainType,
                    wallet.chain.bip44Index,
                    wallet.chain.currency.currencyName,
                    wallet.chain.currency.currencySymbol,
                    wallet.chain.currency.currencyDecimals,
                )
                BitcoinWallet(
                    wallet.wallet.id,
                    account,
                    bitcoinChain,
                    wallet.wallet.name,
                    wallet.wallet.address,
                    wallet.wallet.derivedPath,
                    wallet.wallet.createdAt
                )
            }
            App.ChainType.ETH -> {
                val ethereumChain = EthereumChain(
                    wallet.chain.id,
                    wallet.chain.chainId,
                    wallet.chain.name,
                    wallet.chain.chainType,
                    wallet.chain.bip44Index,
                    wallet.chain.currency.currencyName,
                    wallet.chain.currency.currencySymbol,
                    wallet.chain.currency.currencyDecimals,
                )
                BitcoinWallet(
                    wallet.wallet.id,
                    account,
                    ethereumChain,
                    wallet.wallet.name,
                    wallet.wallet.address,
                    wallet.wallet.derivedPath,
                    wallet.wallet.createdAt
                )
            }
        }
    }

    @OptIn(FlowPreview::class)
    override suspend fun getAccountWallet(chain: IChain): Flow<List<AccountWalletListBean>> {
        val chainId = chain.getId() ?: return emptyFlow()
        val findByTableChainId = walletDao.findByTableChainId(chainId)
        return findByTableChainId.flatMapMerge { wallets ->
            val walletMap = HashMap<Long, MutableList<IWallet>>()
            val accountMap = HashMap<Long, Account>()
            wallets.forEach {
                val convert = convert(it)
                if (walletMap.containsKey(it.account.id)) {
                    walletMap[it.account.id]?.add(convert)
                } else {
                    it.account.id?.let { it1 -> walletMap.put(it1, mutableListOf(convert)) }
                }
                if (!accountMap.containsKey(it.account.id)) {
                    it.account.id?.let { it1 -> accountMap.put(it1, convert.getAccount()) }
                }
            }
            val accountWalletList = mutableListOf<AccountWalletListBean>()
            walletMap.keys.forEach { accountId ->
                accountMap[accountId]?.let {
                    AccountWalletListBean(it, walletMap.getOrElse(accountId) { mutableListOf() })
                }
            }
            flowOf(accountWalletList)
        }
    }
}
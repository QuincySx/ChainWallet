package com.smallraw.chain.wallet.feature.wallets.bean

import com.smallraw.chain.wallet.data.bean.Account
import com.smallraw.chain.wallet.data.bean.IWallet

data class AccountWalletListBean(
    val account: Account,
    val wallets: List<IWallet>
)
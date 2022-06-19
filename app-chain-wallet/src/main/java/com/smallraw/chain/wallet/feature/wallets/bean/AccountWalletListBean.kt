package com.smallraw.chain.wallet.feature.wallets.bean

import com.smallraw.chain.wallet.data.bean.Account
import com.smallraw.chain.wallet.data.bean.Wallet

data class WalletAccountListBean(
    val wallet: Wallet,
    val accounts: List<Account>
)
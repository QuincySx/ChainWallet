package com.smallraw.chain.wallet.feature.wallets

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.smallraw.chain.wallet.data.IWalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WalletSelectListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val walletRepository: IWalletRepository
) : ViewModel() {

}
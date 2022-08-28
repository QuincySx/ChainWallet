package com.smallraw.chain.wallet.ui.screen.home

import androidx.lifecycle.ViewModel
import com.smallraw.chain.wallet.data.IWalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    walletRepository: IWalletRepository
) : ViewModel() {

}
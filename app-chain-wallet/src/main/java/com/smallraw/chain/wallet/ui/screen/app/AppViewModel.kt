package com.smallraw.chain.wallet.ui.screen.app

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallraw.chain.wallet.data.IWalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    walletRepository: IWalletRepository
) : ViewModel() {
    val existsWallet: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    init {
        viewModelScope.launch {
            walletRepository.getWalletCount()
                .onEach { existsWallet.value = it > 0 }
        }
    }
}
package com.smallraw.chain.wallet

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AppViewModel : ViewModel() {
    val data = MutableLiveData<String>()

    init {
        data.value = "init"
    }

}
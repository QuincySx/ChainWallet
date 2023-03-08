package com.smallraw.chain.wallet.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.smallraw.chain.wallet.data.IWalletRepository
import com.smallraw.chain.wallet.designsystem.component.BackgroundSurface
import com.smallraw.chain.wallet.ui.screen.app.AppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateWalletViewModel @Inject constructor(
    private val walletRepository: IWalletRepository,
) : ViewModel() {
    var createWalletId by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun createWallet(name: String, password: String) = viewModelScope.launch {
        isLoading = true
        val wallet = walletRepository.createWallet(name, password)
        isLoading = false
        createWalletId = wallet.id?.toString()
    }
}

@Composable
fun CreateWalletScreen(
    navCtrl: NavHostController,
    appViewModel: AppViewModel,
    onBack: () -> Unit = { navCtrl.popBackStack() },
    createWalletViewModel: CreateWalletViewModel = hiltViewModel(),
) {
    val createWalletId = createWalletViewModel.createWalletId

    LaunchedEffect(createWalletId) {
        if (createWalletId != null) {
            onBack()
        }
    }

    BackgroundSurface {
        Box(contentAlignment = Alignment.BottomEnd) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Button(modifier = Modifier.fillMaxWidth(), onClick = {
                    createWalletViewModel.createWallet("name", "password")
                }) {
                    Text("创建钱包")
                }
            }
        }
    }
}
package com.smallraw.chain.wallet.ui.screen.home

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.smallraw.chain.wallet.designsystem.component.BackgroundSurface
import com.smallraw.chain.wallet.ui.navigation.Screens
import com.smallraw.chain.wallet.ui.screen.AccountManager
import com.smallraw.chain.wallet.ui.screen.app.AppViewModel
import com.xinto.overlappingpanels.OverlappingPanels
import com.xinto.overlappingpanels.rememberOverlappingPanelsState

@Composable
fun HomeScreen(
    navCtrl: NavHostController,
    appViewModel: AppViewModel,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val panelsState = rememberOverlappingPanelsState()
    val gesturesEnabled by remember { mutableStateOf(true) }

    val existsWallet = appViewModel.existsWallet.observeAsState()

    LaunchedEffect(existsWallet) {
        Log.e("HomeScreen", "HomeScreen: " + existsWallet.value)
        if (existsWallet.value == false) {
            navCtrl.navigate(Screens.Welcome.route)
        }
    }

    OverlappingPanels(
        modifier = Modifier.fillMaxSize(),
        panelsState = panelsState,
        gesturesEnabled = gesturesEnabled,
        panelStart = {
            AccountManager()
        },
        panelCenter = {
            BackgroundSurface {
                Text(text = "B")
            }
        },
        panelEnd = {
            BackgroundSurface {
                Text(text = "C")
            }
        },
    )
}
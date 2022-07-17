package com.smallraw.chain.wallet.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.smallraw.chain.wallet.ui.components.PanelSurface
import com.smallraw.chain.wallet.ui.route.Screens
import com.xinto.overlappingpanels.OverlappingPanels
import com.xinto.overlappingpanels.rememberOverlappingPanelsState

@Composable
fun HomeScreen(
    navCtrl: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val panelsState = rememberOverlappingPanelsState()
    val gesturesEnabled by remember { mutableStateOf(true) }

    val existsWallet = homeViewModel.existsWallet.observeAsState()

    LaunchedEffect(existsWallet) {
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
            PanelSurface {
                Text(text = "B")
            }
        },
        panelEnd = {
            PanelSurface {
                Text(text = "C")
            }
        },
    )
}
package com.smallraw.chain.wallet.ui.screen.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.smallraw.chain.wallet.designsystem.theme.AppTheme
import com.smallraw.chain.wallet.ui.navigation.AppNavHost

@Composable
fun AppHome(
    appState: AppState = rememberAppState(),
    appViewModel: AppViewModel = hiltViewModel()
) {
    AppTheme {
        val systemUiController = rememberSystemUiController()
        val isLight = !isSystemInDarkTheme()
        val surface = MaterialTheme.colorScheme.surface

        SideEffect {
            systemUiController.setSystemBarsColor(
                color = surface,
                darkIcons = isLight,
            )
        }

        AppNavHost(
            navController = appState.navController,
        )
    }
}
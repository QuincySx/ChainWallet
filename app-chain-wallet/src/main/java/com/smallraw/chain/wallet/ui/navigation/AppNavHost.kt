package com.smallraw.chain.wallet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.smallraw.chain.wallet.ui.screen.CreateWalletScreen
import com.smallraw.chain.wallet.ui.screen.WelcomeScreen
import com.smallraw.chain.wallet.ui.screen.app.AppState
import com.smallraw.chain.wallet.ui.screen.app.AppViewModel
import com.smallraw.chain.wallet.ui.screen.home.HomeScreen

@Composable
fun AppNavHost(
    appState: AppState,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Screens.Home.route,
    appViewModel: AppViewModel = hiltViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screens.Home.route) { HomeScreen(navController,appViewModel) }
        composable(Screens.Welcome.route) { WelcomeScreen(appState) }
        composable(Screens.CreateWallet.route) {
            CreateWalletScreen(
                navController,
                appViewModel,
                onBack = { appState.navGoBackTo(Screens.Home.route) })
        }
    }
}
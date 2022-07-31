package com.smallraw.chain.wallet.ui.route

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.smallraw.chain.wallet.ui.screen.CreateWalletScreen
import com.smallraw.chain.wallet.ui.screen.HomeScreen
import com.smallraw.chain.wallet.ui.screen.WelcomeScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.Home.route) {
        composable(Screens.Home.route) { HomeScreen(navController) }
        composable(Screens.Welcome.route) { WelcomeScreen(navController) }
        composable(Screens.CreateWallet.route) { CreateWalletScreen(navController) }
    }
}
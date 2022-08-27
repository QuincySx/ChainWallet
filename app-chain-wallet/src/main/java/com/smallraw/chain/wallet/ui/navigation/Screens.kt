package com.smallraw.chain.wallet.ui.navigation

sealed class Screens(override val route: String, override val destination: String? = "") :
    NavigationDestination {
    object Home : Screens("home")
    object Welcome : Screens("welcome")
    object CreateWallet : Screens("CreateWallet")
}
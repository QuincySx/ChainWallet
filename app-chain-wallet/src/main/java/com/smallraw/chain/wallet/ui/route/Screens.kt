package com.smallraw.chain.wallet.ui.route

sealed class Screens(val route: String) {
    object Home : Screens("home")
    object Welcome : Screens("welcome")
    object CreateWallet : Screens("CreateWallet")
}
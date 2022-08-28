package com.smallraw.chain.wallet.ui.screen.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.core.os.trace
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.smallraw.chain.wallet.ui.components.JankMetricDisposableEffect
import com.smallraw.chain.wallet.ui.navigation.NavigationDestination

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController()
): AppState {
    NavigationTrackingSideEffect(navController)
    return remember(navController) {
        AppState(navController)
    }
}

@Stable
class AppState(
    public val navController: NavHostController,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    fun navigate(destination: NavigationDestination, route: String? = null) {
        trace("Navigation: $destination") {
            navController.navigate(route ?: destination.route)
        }
    }

    fun navGoBack() {
        navController.popBackStack()
    }

    fun navGoBackTo(route: String) {
        navController.popBackStack(route, false)
    }
}

@Composable
private fun NavigationTrackingSideEffect(navController: NavHostController) {
    JankMetricDisposableEffect(navController) { metricsHolder ->
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            metricsHolder.state?.putState("Navigation", destination.route.toString())
        }

        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
}
package com.example.mvi_compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

object MainDestinations {
    const val HOME = "Home"
    const val EXCHANGE_RATES = "ExchangeRates"
    const val USER_WALLET_INFO = "UserWalletInfo"
}

object NavArguments {
    const val USER_ID = "userId"
}

@Composable
fun rememberMainAppState(
    navController: NavHostController = rememberNavController()
) =
    remember(navController) {
        MainAppState(navController)
    }

@Stable
class MainAppState(
    val navController: NavHostController
) {

    val currentRoute: String?
        get() = navController.currentDestination?.route

    fun upPress() {
        navController.currentBackStackEntry?.let {
            navController.navigateUp()
        }
    }

    fun navigateToUserWalletInfo(route: String, movieId: Long) {
        if (route != currentRoute) {
            navController.navigate("$route/$movieId") {
                launchSingleTop = true
                restoreState = true
//                if (popPreviousScreen) {
//                    popUpTo(navController.currentBackStackEntry?.destination?.route ?: return@navigate) {
//                        inclusive = true
//                    }
//                }
            }
        }
    }

    fun navigateToExchangeRates(route: String) {
        if (route != currentRoute) {
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
            }
        }
    }


    fun navigateToRoute(route: String) {
        if (route != currentRoute) {
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
            }
        }
    }
}



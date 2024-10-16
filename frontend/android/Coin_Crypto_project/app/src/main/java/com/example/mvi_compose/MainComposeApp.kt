package com.example.mvi_compose

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.mvi_compose.ui.exchange_rates.ExchangeRatesScreen
import com.example.mvi_compose.ui.user_wallet_info.UserWalletInfoScreen
import com.example.mvi_compose.ui.users.UsersScreen

data class BottomNavigationBarItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeAmount: Int? = null
)

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MainComposeApp(
) {
    val appState = rememberMainAppState()

    val navBackStackEntry =
        appState.navController.currentBackStackEntryAsState() // navController.currentBackStackEntryAsState()


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Scaffold()
        { paddingValues ->
            NavHost(
                navController = appState.navController,
                startDestination = MainDestinations.HOME,
                modifier = Modifier.padding(paddingValues)
            ) {
                mainNavGraph(
                    navBackStackEntry = navBackStackEntry,
                    goToExchangeRates = { route ->
                        appState.navigateToExchangeRates(route = route)
                    },
                    goToUserWalletInfo = { route, movieId ->
                        appState.navigateToUserWalletInfo(route = route, movieId = movieId)
                    },
                    navigateUp = {
                        appState.upPress()
                    }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.mainNavGraph(
    navBackStackEntry: State<NavBackStackEntry?>,
    goToUserWalletInfo: (route: String, movieId: Long) -> Unit,
    goToExchangeRates: (route: String) -> Unit,
    navigateUp: () -> Unit
) {
    composable(MainDestinations.HOME) {
        UsersScreen(
            viewModel = hiltViewModel(),
            onExchangeRateClick = {
                goToExchangeRates(MainDestinations.EXCHANGE_RATES)
            },
            onUserClick = { userId ->
                if (navBackStackEntry.value?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                    goToUserWalletInfo(MainDestinations.USER_WALLET_INFO, userId)
                }
            })
    }

    composable(MainDestinations.EXCHANGE_RATES) {
        ExchangeRatesScreen(viewModel = hiltViewModel())
    }

    composable(
        "${MainDestinations.USER_WALLET_INFO}/{${NavArguments.USER_ID}}",
        arguments = listOf(navArgument(NavArguments.USER_ID) {
            type = NavType.LongType
        })
    ) { backStackEntry ->
        val userId = backStackEntry.arguments?.getLong(NavArguments.USER_ID) ?: 0L
        UserWalletInfoScreen(
            viewModel = hiltViewModel(),
            userId = userId
//            navigateUp = {
//                navigateUp()
//            }
        )
    }

//    composable(
//        "${MainDestinations.MOVIE_DETAILS}/{${NavArguments.MOVIE_ID}}",
//        arguments = listOf(navArgument(NavArguments.MOVIE_ID) {
//            type = NavType.LongType
//        })
//    ) {
//        MovieDetailsScreen(
//            viewModel = hiltViewModel(),
//            navigateUp = {
//                navigateUp()
//            }
//        )
//    }


}


// ----------------------------------------
// This is a wrapper view that allows us to easily and cleanly
// reuse this component in any future project
@Composable
fun TabView(
    tabBarItems: List<BottomNavigationBarItem>,
    navBackStackEntry: State<NavBackStackEntry?>,
    goToNextScreen: (route: String) -> Unit
) {

    NavigationBar {
        // looping over each tab to generate the views and navigation for each item
        tabBarItems.forEachIndexed { _, tabBarItem ->
            NavigationBarItem(
                selected = tabBarItem.title == navBackStackEntry.value?.destination?.route, // selectedTabIndex == index,
                onClick = {
                    goToNextScreen(tabBarItem.title)
                },
                icon = {
                    TabBarIconView(
                        isSelected = tabBarItem.title == navBackStackEntry.value?.destination?.route, // selectedTabIndex == index,
                        selectedIcon = tabBarItem.selectedIcon,
                        unselectedIcon = tabBarItem.unselectedIcon,
                        title = tabBarItem.title,
                        badgeAmount = tabBarItem.badgeAmount
                    )
                },
                label = { Text(tabBarItem.title) })
        }
    }
}

// This component helps to clean up the API call from our TabView above,
// but could just as easily be added inside the TabView without creating this custom component
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabBarIconView(
    isSelected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    title: String,
    badgeAmount: Int? = null
) {
    BadgedBox(badge = {
        TabBarBadgeView(badgeAmount)
    }) {
        Icon(
            imageVector = if (isSelected) {
                selectedIcon
            } else {
                unselectedIcon
            },
            contentDescription = title
        )
    }
}

// This component helps to clean up the API call from our TabBarIconView above,
// but could just as easily be added inside the TabBarIconView without creating this custom component
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TabBarBadgeView(count: Int? = null) {
    if (count != null) {
        Badge {
            Text(count.toString())
        }
    }
}
// end of the reusable components that can be copied over to any new projects
// ----------------------------------------


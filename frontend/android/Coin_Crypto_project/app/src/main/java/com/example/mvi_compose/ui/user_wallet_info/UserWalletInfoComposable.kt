package com.example.mvi_compose.ui.user_wallet_info

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mvi_compose.R
import com.example.mvi_compose.network.data.WalletResponse
import com.example.mvi_compose.ui.UiEffect
import com.example.mvi_compose.ui.coin_cryptos.UserWalletEvents
import com.example.mvi_compose.ui.coin_cryptos.UserWalletViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserWalletInfoScreen(
    viewModel: UserWalletViewModel,
    userId: Long
) {

    Log.d("USER_ID", "UserWalletInfoScreen: $userId")
    Log.d("USER_ID", "UserWalletInfoScreen")
    val userWalletState = viewModel.state.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.onEvent(UserWalletEvents.FetchUserById(userId))
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.uiEffect.collect { event ->
            when (event) {
                is UiEffect.ShowToast -> {
                    Toast.makeText(context, event.message, event.toastLength).show()
                }
            }
        }
    }

    userWalletState.let {
        if (userWalletState.loading) {
            LoadingScreen()
        } else if (userWalletState.error.isNotEmpty()) {
            ErrorScreen(error = userWalletState.error)
        } else if (userWalletState.walletList.isNotEmpty()) {
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {

                val (welcomeText, userInfo, walletInfo, userList) = createRefs()
                val backgroundColor = colorResource(R.color.teal_700)

                // Welcome Text
                Text(
                    text = "User and wallet info",
                    modifier = Modifier
                        .constrainAs(welcomeText) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .padding(10.dp)
                        .background(backgroundColor)
                        .padding(10.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )

                // User Info
                if (userWalletState.user.postal.isNotEmpty()) {
                    Text(
                        text = userWalletState.user.postal,
                        modifier = Modifier
                            .constrainAs(userInfo) {
                                top.linkTo(welcomeText.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .padding(10.dp)
                            .background(backgroundColor),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }

                // Wallet Info
//                if (userWalletState.walletInfo.idUser > 0) {
//                    Text(
//                        text = userWalletState.walletInfo.idUser.toString(), // Customize display as needed
//                        modifier = Modifier
//                            .constrainAs(walletInfo) {
//                                top.linkTo(userInfo.bottom)
//                                start.linkTo(parent.start)
//                                end.linkTo(parent.end)
//                            }
//                            .padding(10.dp)
//                            .background(backgroundColor),
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = Color.White
//                    )
//                }

                // LazyColumn for walletList
                if (userWalletState.walletList.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .constrainAs(userList) {
                                top.linkTo(userInfo.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
//                                bottom.linkTo(parent.bottom)
                            }
                            .wrapContentSize()
                            .padding(8.dp)
                    ) {

                        items(
                            items = userWalletState.walletList,
                            key = { wallet -> wallet.id }
                        ) { wallet ->
                            MoviesListScreen(
                                user = wallet,
                                backgroundColor = backgroundColor
                            )
                        }
                    }
                }

//                val (welcomeText, userList, checkExchangeRates) = createRefs()
//                val backgroundColor = colorResource(R.color.teal_700)
//
//                Text(
//                    text = "User and wallet info",
//                    modifier = Modifier
//                        .constrainAs(welcomeText) {
//                            top.linkTo(parent.top)
//                            start.linkTo(parent.start)
//                            end.linkTo(parent.end)
////                            bottom.linkTo(userList.top)
//                        }
//                        .padding(10.dp)
//                        .background(backgroundColor)
//                        .padding(10.dp),
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = Color.White
//                )
//
//
//
//                Text(
//                    text = userWalletState.user.postal,
//                    modifier = Modifier
//                        .constrainAs(checkExchangeRates) {
//                            top.linkTo(welcomeText.bottom)
//                            start.linkTo(parent.start)
//                            end.linkTo(parent.end)
//                        }
//                        .padding(10.dp)
//                        .background(backgroundColor),
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = Color.White
//                )
//
//                val walletList = remember { userWalletState.walletList }
//                val listState = rememberLazyListState()
//
//                LazyColumn(
//                    state = listState,
//                    modifier = Modifier
//                        .constrainAs(userList) {
//                            top.linkTo(checkExchangeRates.bottom)
//                            start.linkTo(parent.start)
//                            end.linkTo(parent.end)
//                            bottom.linkTo(parent.bottom)
//                        }
//                        .wrapContentSize()
//                        .padding(8.dp),
//                    verticalArrangement = Arrangement.spacedBy(4.dp)
//                ) {
//                    items(
//                        items = walletList,
//                        key = { user -> user.id }
//                    ) { user ->
//                        MoviesListScreen(
//                            user = user,
//                            backgroundColor = backgroundColor
//                        )
//                    }
//                }
            }
        }
    }
}

@Composable
fun MoviesListScreen(
    user: WalletResponse,
    backgroundColor: Color,
) {
    Log.d("USER_ID", "Recompose user id is 1: ${user.id.toLong()}")

    Text(
        text = "Full Name: ${user.coinToken} \nCity is: ${user.countTimeStamp}",
        fontSize = 14.sp,
        modifier = Modifier
            .padding(horizontal = 40.dp, vertical = 10.dp)
            .background(backgroundColor)
            .padding(10.dp) ,
        color = Color.White
    )
}

@Composable
fun LoadingScreen() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun ErrorScreen(error: String) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Text(text = error, color = MaterialTheme.colorScheme.error)
    }
}

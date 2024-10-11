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
import com.example.mvi_compose.ui.users.ErrorScreen
import com.example.mvi_compose.ui.users.LoadingScreen
import com.example.mvi_compose.util.BackgroundColor
import com.example.mvi_compose.util.TextBlackColor
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

                val (welcomeText, userInfo, walletInfo, spacer, userList) = createRefs()
                val backgroundColor = colorResource(R.color.teal_700)

                // Welcome Text
                Card(
                    modifier = Modifier
                        .constrainAs(welcomeText) {
                            top.linkTo(parent.top, margin = 20.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    colors = CardDefaults.cardColors(containerColor = backgroundColor)
                ) {
                    Text(
                        text = "User and wallet info",
                        modifier = Modifier
                            .padding(10.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }

                // User Info
                Column(
                    modifier = Modifier
                        .constrainAs(userInfo) {
                            top.linkTo(welcomeText.bottom, margin = 5.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .padding(10.dp)
                        .background(BackgroundColor)
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Full Name: ${userWalletState.user.fullName}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextBlackColor
                    )
                    Text(
                        text = "Address: ${userWalletState.user.address}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextBlackColor
                    )
                    Text(
                        text = "City: ${userWalletState.user.city} Postal: ${userWalletState.user.postal}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextBlackColor
                    )
//                    Text(
//                        text = "Postal: ${userWalletState.user.postal}",
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = TextBlackColor
//                    )
                }

                // Wallet Info
                Column(
                    modifier = Modifier
                        .constrainAs(walletInfo) {
                            top.linkTo(userInfo.bottom, margin = 5.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .background(BackgroundColor)
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Total Worth: ${userWalletState.walletInfo.totalWorth}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextBlackColor
                    )

                    if( userWalletState.walletInfo.lastPurchase.countTimeStamp.isNotEmpty() ) {
                        val formattedDate = formatDateTime(userWalletState.walletInfo.lastPurchase.countTimeStamp)
                        Text(
                            text = "Last Purchase: $formattedDate",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextBlackColor
                        )
                    }
                }

                // Spacer
                Spacer(
                    modifier = Modifier
                        .constrainAs(spacer) {
                            top.linkTo(walletInfo.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .height(4.dp)
                )

                // Wallet List
                if (userWalletState.walletList.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .constrainAs(userList) {
                                top.linkTo(spacer.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .wrapContentSize()
                            .padding(8.dp)
                    ) {
                        items(
                            items = userWalletState.walletList,
                            key = { wallet -> wallet.id }
                        ) { wallet ->
                            WalletsListScreen(
                                user = wallet
                            )
                        }
                    }
                }
            }
            /*ConstraintLayout(modifier = Modifier.fillMaxSize()) {

                val (welcomeText, userInfo, walletInfo, spacer, userList) = createRefs()
                val backgroundColor = colorResource(R.color.teal_700)

                // Welcome Text
                Card(
                    modifier = Modifier
                        .constrainAs(welcomeText) {
                            top.linkTo(parent.top, margin = 20.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    colors = CardDefaults.cardColors(containerColor = backgroundColor )
                ) {
                    Text(
                        text = "User and wallet info",
                        modifier = Modifier
                            .padding(10.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }

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
                            .background(BackgroundColor),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextBlackColor
                    )
                }

                // Wallet Info
                if (userWalletState.walletInfo.lastPurchase.countTimeStamp.isNotEmpty()) {

                    val formattedDate = formatDateTime(userWalletState.walletInfo.lastPurchase.countTimeStamp)

                    Text(
                        text = formattedDate,
                        modifier = Modifier
                            .constrainAs(walletInfo) {
                                top.linkTo(userInfo.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .background(BackgroundColor),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextBlackColor
                    )
                }

                // Spacer
                Spacer(
                    modifier = Modifier
                        .constrainAs(spacer) {
                            top.linkTo(walletInfo.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .height(8.dp)
                )

                if (userWalletState.walletList.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .constrainAs(userList) {
                                top.linkTo(spacer.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .wrapContentSize()
                            .padding(8.dp)
                    ) {

                        items(
                            items = userWalletState.walletList,
                            key = { wallet -> wallet.id }
                        ) { wallet ->
                            WalletsListScreen(
                                user = wallet
                            )
                        }
                    }
                }
            }*/
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateTime(dateTime: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    val parsedDateTime = LocalDateTime.parse(dateTime, formatter)
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return parsedDateTime.format(outputFormatter)
}

@Composable
fun WalletsListScreen(
    user: WalletResponse
) {
    Log.d("USER_ID", "Recompose user id is 1: ${user.id.toLong()}")

    Text(
        text = "Full Name: ${user.coinToken} \nCity is: ${user.countTimeStamp}",
        fontSize = 14.sp,
        modifier = Modifier
            .padding(horizontal = 40.dp, vertical = 10.dp)
            .background(BackgroundColor)
            .padding(10.dp) ,
        color = TextBlackColor
    )
}

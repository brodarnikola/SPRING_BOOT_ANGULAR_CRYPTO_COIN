package com.example.mvi_compose.ui.users

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.example.mvi_compose.network.data.UserResponse
import com.example.mvi_compose.ui.UiEffect
import com.example.mvi_compose.ui.coin_cryptos.ExchangeRateViewModel
import com.example.mvi_compose.ui.coin_cryptos.UsersViewModel

@Composable
fun UsersScreen(
    viewModel: UsersViewModel,
    onUserClick: (id: Long) -> Unit,
    onExchangeRateClick: () -> Unit)
{
    Log.d("USER_ID", "user id is 22: ${onUserClick}")
    val usersRateState = viewModel.state.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.uiEffect.collect { event ->
            when (event) {
                is UiEffect.ShowToast -> {
                    Toast.makeText(context, event.message, event.toastLength).show()
                }
            }
        }
    }

    usersRateState.let {
        if (usersRateState.loading) {
            LoadingScreen()
        } else if (usersRateState.error.isNotEmpty()) {
            ErrorScreen(error = usersRateState.error)
        } else if (usersRateState.users.isNotEmpty()) {
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (welcomeText, userList, checkExchangeRates) = createRefs()
                val backgroundColor = colorResource(R.color.teal_700)

                Text(
                    text = "Welcome to coin crypto project",
                    modifier = Modifier
                        .constrainAs(welcomeText) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(userList.top)
                        }
                        .padding(10.dp)
                        .background(backgroundColor)
                        .padding(10.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )

                val finalUsersList = remember { usersRateState.users }
                val listState = rememberLazyListState()

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .constrainAs(userList) {
                            top.linkTo(welcomeText.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(checkExchangeRates.top)
                        }
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(
                        items = finalUsersList,
                        key = { user -> user.id }
                    ) { user ->
                        MoviesListScreen(
                            onUserClick = onUserClick,
                            user = user,
                            backgroundColor = backgroundColor
                        )
                    }
                }

                Text(
                    text = "Check exchange rates",
                    modifier = Modifier
                        .constrainAs(checkExchangeRates) {
                            top.linkTo(userList.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                        .padding(10.dp)
                        .background(backgroundColor)
                        .clickable(onClick = {
                            Log.d("USER_ID", "onExchangeRateClick:  ")
                            onExchangeRateClick()
                        }),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun MoviesListScreen(
    onUserClick: (id: Long) -> Unit,
    user: UserResponse,
    backgroundColor: Color,
) {
    Log.d("USER_ID", "Recompose user id is 1: ${user.id.toLong()}")

    Text(
        text = "Full Name: ${user.fullName} \nCity is: ${user.city}",
        fontSize = 14.sp,
        modifier = Modifier
            .padding(horizontal = 40.dp, vertical = 10.dp)
            .background(backgroundColor)
            .padding(10.dp)
            .clickable(onClick = {
                Log.d("USER_ID", "User id is 11: ${user.id}")
                onUserClick(user.id.toLong())
            }),
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

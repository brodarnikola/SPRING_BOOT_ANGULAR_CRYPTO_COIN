package com.example.mvi_compose.ui.exchange_rates

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mvi_compose.R
import com.example.mvi_compose.network.data.ExchangeRatesResponse
import com.example.mvi_compose.ui.UiEffect
import com.example.mvi_compose.ui.coin_cryptos.ExchangeRateViewModel
import com.example.mvi_compose.ui.dialogs.FilterDialog
import com.example.mvi_compose.ui.users.ErrorScreen
import com.example.mvi_compose.ui.users.LoadingScreen
import com.example.mvi_compose.util.BackgroundColor
import com.example.mvi_compose.util.TextBlackColor
import com.example.mvi_compose.util.TextBlueColor
import com.example.mvi_compose.util.TextGreenColor
import com.example.mvi_compose.util.TextRedColor

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExchangeRatesScreen(viewModel: ExchangeRateViewModel) {
    Log.d("EXCHANGE_RATES", "ExchangeRateViewModel")
    val exchangeRateState = viewModel.state.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }
    var eurFilter by remember { mutableStateOf("") }
    var usdFilter by remember { mutableStateOf("") }
    var gbpFilter by remember { mutableStateOf("") }

    LaunchedEffect(key1 = Unit) {
        viewModel.uiEffect.collect { event ->
            when (event) {
                is UiEffect.ShowToast -> {
                    Toast.makeText(context, event.message, event.toastLength).show()
                }
            }
        }
    }

    exchangeRateState.let {
        if (exchangeRateState.loading) {
            LoadingScreen()
        } else if (exchangeRateState.error.isNotEmpty()) {
            ErrorScreen(error = exchangeRateState.error)
        } else if (exchangeRateState.exchangeRates.isNotEmpty()) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                val (filterSection, exchangeRatesListRef) = createRefs()
                val backgroundColor = colorResource(R.color.teal_700)

                Row(modifier = Modifier
                    .constrainAs(filterSection) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround) {

                    Card(
                        colors = CardDefaults.cardColors(backgroundColor )
                    ) {
                        Text(
                            text = "Exchange Rate Analytics",
                            modifier = Modifier
                                .padding(10.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Card(
                        colors = CardDefaults.cardColors(backgroundColor )
                    ) {
                        Text(
                            text = "Filter",
                            modifier = Modifier
                                .background(backgroundColor)
                                .clickable {
                                    showDialog = true
                                }
                                .padding(10.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                    }
                }

                val listState = rememberLazyListState()

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .constrainAs(exchangeRatesListRef) {
                            top.linkTo(filterSection.bottom, margin = 36.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = exchangeRateState.exchangeRates,
                        key = { rate -> rate.id }
                    ) { rate ->
                        ExchangeRateItem(
                            rate = rate,
                            eurMedian = exchangeRateState.eurMedian,
                            usdMedian = exchangeRateState.usdMedian,
                            gbpMedian = exchangeRateState.gbpMedian
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        FilterDialog(
            onDismiss = { showDialog = false },
            onApply = { eur, usd, gbp, dateFrom, dateTo ->
                eurFilter = eur
                usdFilter = usd
                gbpFilter = gbp
                viewModel.applyFilters(eurFilter, usdFilter, gbpFilter, dateFrom, dateTo)
                showDialog = false
            },
        )
    }
}

@Composable
fun ExchangeRateItem(
    rate: ExchangeRatesResponse,
    eurMedian: Float,
    usdMedian: Float,
    gbpMedian: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundColor )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Date: ${rate.excRateDate}",
                style = MaterialTheme.typography.labelMedium,
                color = TextBlackColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "EUR Rate: ${rate.excRateEur} (Median: $eurMedian)",
                style = MaterialTheme.typography.labelMedium,
                color = getColor(rate.excRateEur, eurMedian)
            )
            Text(
                text = "USD Rate: ${rate.excRateUsd} (Median: $usdMedian)",
                style = MaterialTheme.typography.labelMedium,
                color = getColor(rate.excRateUsd, usdMedian)
            )
            Text(
                text = "GBP Rate: ${rate.excRateGbp} (Median: $gbpMedian)",
                style = MaterialTheme.typography.labelMedium,
                color = getColor(rate.excRateGbp, gbpMedian)
            )
        }
    }
}

// Function to determine color based on value and median
@Composable
fun getColor(rate: Float, median: Float): Color {
    return when {
        rate < median -> TextRedColor
        rate > median -> TextGreenColor
        else -> TextBlueColor
    }
}

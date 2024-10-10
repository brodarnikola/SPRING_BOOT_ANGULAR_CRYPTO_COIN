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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
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
                    },
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = "Exchange Rate Analytics",
                        modifier = Modifier
                            .background(backgroundColor)
                            .padding(10.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )

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

                val filteredExchangeRates = exchangeRateState.exchangeRates.filter { rate ->
                    (eurFilter.isEmpty() || rate.excRateEur.toString().startsWith(eurFilter)) &&
                            (usdFilter.isEmpty() || rate.excRateUsd.toString()
                                .startsWith(usdFilter)) &&
                            (gbpFilter.isEmpty() || rate.excRateGbp.toString()
                                .startsWith(gbpFilter))
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
                        items = filteredExchangeRates,
                        key = { rate -> rate.id }
                    ) { rate ->
                        ExchangeRateItem(
                            rate = rate,
                            eurMedian = exchangeRateState.eurMedian,
                            usdMedian = exchangeRateState.usdMedian,
                            gbpMedian = exchangeRateState.gbpMedian,
                            backgroundColor = backgroundColor
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
                viewModel.dateFrom.value = dateFrom
                viewModel.dateTo.value = dateTo
                viewModel.onDateChange()
                showDialog = false
            },
            dateFrom = viewModel.dateFrom.value,
            dateTo = viewModel.dateTo.value
        )
    }
}

@Composable
fun FilterDialog(
    onDismiss: () -> Unit,
    onApply: (String, String, String, String, String) -> Unit,
    dateFrom: String,
    dateTo: String
) {
    var eurFilter by remember { mutableStateOf("") }
    var usdFilter by remember { mutableStateOf("") }
    var gbpFilter by remember { mutableStateOf("") }
    var dateFromState by remember { mutableStateOf(dateFrom) }
    var dateToState by remember { mutableStateOf(dateTo) }


    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Filter Exchange Rates") },
        text = {
            Column {
                OutlinedTextField(
                    value = eurFilter,
                    onValueChange = { eurFilter = it },
                    label = { Text("EUR Rate Filter") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = usdFilter,
                    onValueChange = { usdFilter = it },
                    label = { Text("USD Rate Filter") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = gbpFilter,
                    onValueChange = { gbpFilter = it },
                    label = { Text("GBP Rate Filter") },
                    modifier = Modifier.fillMaxWidth()
                )
                DatePickerField(
                    label = "From Date",
                    date = dateFromState,
                    onDateChange = { dateFromState = it }
                )
                DatePickerField(
                    label = "To Date",
                    date = dateToState,
                    onDateChange = { dateToState = it }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onApply(
                    eurFilter,
                    usdFilter,
                    gbpFilter,
                    dateFromState,
                    dateToState
                )
            }) {
                Text("Apply")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
@Composable
fun DatePickerField(
    label: String,
    date: String,
    onDateChange: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = java.util.Calendar.getInstance()
    val dateFormatter = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())

    // Parse the initial date
    val initialDate = try {
        dateFormatter.parse(date)?.let {
            calendar.time = it
            calendar
        } ?: java.util.Calendar.getInstance()
    } catch (e: Exception) {
        java.util.Calendar.getInstance()
    }

    val year = initialDate.get(java.util.Calendar.YEAR)
    val month = initialDate.get(java.util.Calendar.MONTH)
    val day = initialDate.get(java.util.Calendar.DAY_OF_MONTH)

    val datePickerDialog = remember {
        android.app.DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                onDateChange(dateFormatter.format(calendar.time))
            },
            year,
            month,
            day
        )
    }

    OutlinedTextField(
        value = date,
        onValueChange = {},
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { datePickerDialog.show() }, // Open DatePicker on click
        readOnly = true,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange, // Use your icon here
                contentDescription = "Select date",
                modifier = Modifier.clickable { datePickerDialog.show() } // Icon click listener
            )
        }
    )
}

@Composable
fun ExchangeRateItem(
    rate: ExchangeRatesResponse,
    eurMedian: Float,
    usdMedian: Float,
    gbpMedian: Float,
    backgroundColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(
            modifier = Modifier
                .background(backgroundColor)
                .padding(16.dp)
        ) {
            Text(
                text = "Date: ${rate.excRateDate}",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "EUR Rate: ${rate.excRateEur} (Median: $eurMedian)",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
            Text(
                text = "USD Rate: ${rate.excRateUsd} (Median: $usdMedian)",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
            Text(
                text = "GBP Rate: ${rate.excRateGbp} (Median: $gbpMedian)",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
        }
    }
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

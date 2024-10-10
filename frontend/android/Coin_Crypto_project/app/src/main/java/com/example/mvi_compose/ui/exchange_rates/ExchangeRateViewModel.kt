package com.example.mvi_compose.ui.coin_cryptos

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewModelScope
import com.example.mvi_compose.repositories.UserRepoImpl
import com.example.mvi_compose.network.NetworkResult
import com.example.mvi_compose.network.data.ExchangeRatesResponse
import com.example.mvi_compose.network.data.UserResponse
import com.example.mvi_compose.repositories.ExchangeRatesRepoImpl
import com.example.mvi_compose.ui.BaseViewModel
import com.example.mvi_compose.ui.UiEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ExchangeRateViewModel @Inject constructor(
    private val exchangeRatesRepo: ExchangeRatesRepoImpl
) : BaseViewModel<ExchangeRateState, ExchangeRatesEvents>() {

    override fun initialState(): ExchangeRateState {
        return ExchangeRateState()
    }

    init {
        onEvent(ExchangeRatesEvents.FetchAllExchangeRates)
    }

    override fun onEvent(event: ExchangeRatesEvents) {
        when (event) {

            ExchangeRatesEvents.FetchAllExchangeRates ->  {
                _state.update { it.copy(loading = true) }
                viewModelScope.launch(Dispatchers.IO) {
                    delay(1500)
                    when (val result = exchangeRatesRepo.getExchangeRates()) {

                        is NetworkResult.Error -> {
                            Log.d("coin_crypto", "apiError is: ${result.apiError}")
                            Log.d("coin_crypto", "message is: ${result.message}")
                            _state.update { it.copy(loading = false, error = result.message ?: "There is error occured, please try again") }
                        }

                        is NetworkResult.Exception -> {
                            Log.d("coin_crypto", "apiError is 1: ${result.e}")
                            Log.d("coin_crypto", "message is 2: ${result.e.localizedMessage}")
                            _state.update { it.copy(loading = false, error = result.e.localizedMessage ?: "There is error occured, please try again") }
                        }

                        is NetworkResult.Success -> {
                            withContext(Dispatchers.Main) {

                                val exchangeRates = result.data.toMutableStateList()
                                // Keep the original data for filtering later
                                _state.update {
                                    it.copy(
                                        originalExchangeRates = exchangeRates
                                    )
                                }

                                val eurMedian = calculateMedian(exchangeRates, "EUR")
                                val usdMedian = calculateMedian(exchangeRates, "USD")
                                val gbpMedian = calculateMedian(exchangeRates, "GBP")

                                _state.update {
                                    it.copy(
                                        loading = false,
                                        exchangeRates = exchangeRates,
                                        eurMedian = eurMedian,
                                        usdMedian = usdMedian,
                                        gbpMedian = gbpMedian
                                    )
                                }
                            }
                            sendUiEvent(UiEffect.ShowToast("Fetched all exchange rates"))
                        }
                    }
                }
            }

            else -> {}
        }
    }

    fun applyFilters(eurFilter: String, usdFilter: String, gbpFilter: String, dateFromFilter: String, dateToFilter: String) {
        val filteredRates = _state.value.originalExchangeRates.filter { rate ->
            (eurFilter.isEmpty() || rate.excRateEur.toString().startsWith(eurFilter)) &&
                    (usdFilter.isEmpty() || rate.excRateUsd.toString().startsWith(usdFilter)) &&
                    (gbpFilter.isEmpty() || rate.excRateGbp.toString().startsWith(gbpFilter)) &&
                    (dateFromFilter.isEmpty() || rate.excRateDate >= dateFromFilter) &&
                    (dateToFilter.isEmpty() || rate.excRateDate <= dateToFilter)
        }

        _state.update { it.copy(exchangeRates = filteredRates.toMutableList()) }
    }

}

fun calculateMedian(rates: List<ExchangeRatesResponse>, currencyType: String): Float {
    val sortedRates = when (currencyType) {
        "EUR" -> rates.map { it.excRateEur }.sorted()
        "USD" -> rates.map { it.excRateUsd }.sorted()
        "GBP" -> rates.map { it.excRateGbp }.sorted()
        else -> emptyList()
    }

    return if (sortedRates.isNotEmpty()) {
        val middle = sortedRates.size / 2
        if (sortedRates.size % 2 == 0) {
            (sortedRates[middle - 1] + sortedRates[middle]) / 2
        } else {
            sortedRates[middle]
        }
    } else {
        0f // Handle empty case
    }
}

//private fun calculateMedian(values: List<Float>): Float {
//    val sortedValues = values.sorted()
//    val mid = sortedValues.size / 2
//    return if (sortedValues.size % 2 == 0) {
//        (sortedValues[mid - 1] + sortedValues[mid]) / 2
//    } else {
//        sortedValues[mid]
//    }
//}

sealed class ExchangeRatesEvents {
    object FetchAllExchangeRates: ExchangeRatesEvents()
}

data class ExchangeRateState(

    val eurMedian: Float = 0f,
    val usdMedian: Float = 0f,
    val gbpMedian: Float = 0f,
    val exchangeRates: MutableList<ExchangeRatesResponse> = mutableListOf(),
    val originalExchangeRates: MutableList<ExchangeRatesResponse> = mutableListOf(),

    val loading: Boolean = false,
    val error: String = ""
)
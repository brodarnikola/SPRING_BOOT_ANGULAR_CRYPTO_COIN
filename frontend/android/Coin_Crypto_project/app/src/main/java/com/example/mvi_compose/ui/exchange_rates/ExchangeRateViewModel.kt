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

    var dateFrom = mutableStateOf("")
    var dateTo = mutableStateOf("")

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
                                val eurMedian = calculateMedian(exchangeRates.map { it.excRateEur })
                                val usdMedian = calculateMedian(exchangeRates.map { it.excRateUsd })
                                val gbpMedian = calculateMedian(exchangeRates.map { it.excRateGbp })

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

    fun onDateChange() {
        onEvent(ExchangeRatesEvents.FetchAllExchangeRates)
//        fetchExchangeRates()
    }
}

private fun calculateMedian(values: List<Float>): Float {
    val sortedValues = values.sorted()
    val mid = sortedValues.size / 2
    return if (sortedValues.size % 2 == 0) {
        (sortedValues[mid - 1] + sortedValues[mid]) / 2
    } else {
        sortedValues[mid]
    }
}

sealed class ExchangeRatesEvents {
    object FetchAllExchangeRates: ExchangeRatesEvents()
}

data class ExchangeRateState(

    val eurMedian: Float = 0f,
    val usdMedian: Float = 0f,
    val gbpMedian: Float = 0f,
    val exchangeRates: MutableList<ExchangeRatesResponse> = mutableListOf(),

    val loading: Boolean = false,
    val error: String = ""
)
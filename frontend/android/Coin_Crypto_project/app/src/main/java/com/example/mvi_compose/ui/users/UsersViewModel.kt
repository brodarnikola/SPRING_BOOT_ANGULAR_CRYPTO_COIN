package com.example.mvi_compose.ui.coin_cryptos

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
class UsersViewModel @Inject constructor(
    private val userRepo: UserRepoImpl,
    private val exchangeRatesRepo: ExchangeRatesRepoImpl
) : BaseViewModel<UsersState, UsersEvents>() {

    override fun initialState(): UsersState {
        return UsersState()
    }

    init {
        onEvent(UsersEvents.FetchAllUsers)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onEvent(event: UsersEvents) {
        when (event) {
            UsersEvents.FetchAllUsers -> {
                _state.update { it.copy(loading = true) }
                viewModelScope.launch(Dispatchers.IO) {
                    delay(1500)
                    when (val result = userRepo.getUsers()) {

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
                                _state.update {
                                    it.copy(
                                        loading = false,
                                        users = result.data.toMutableStateList()
                                    )
                                }
                            }


                       /*     val listFetchImages = mutableListOf<Deferred<Unit>>()
                            result.data.forEachIndexed { index, _ ->

                                listFetchImages.add(
                                    async {
                                        val increment = if (index % 2 == 0) 2000 else 1000
                                        val random = Random.nextInt(200) + increment
                                        Log.d(
                                            REST_API_CALL,
                                            "Random delay is START: ${random} .. ${_state.value.coin_cryptos[index]}"
                                        )
                                        delay(random.toLong())
                                        Log.d(
                                            REST_API_CALL,
                                            "Random delay is FINISH: ${_state.value.coin_cryptos[index]}"
                                        )

                                        withContext(Dispatchers.Main) {
                                            _state.value.coin_cryptos[index] = _state.value.coin_cryptos[index].copy(random_delay = random.toLong())
//                                            _state.update { it.copy( coin_cryptos = _state.value.coin_cryptos
//                                            ) }
                                           *//* _state.value.coin_cryptos[index] =
                                                _state.value.coin_cryptos[index].copy(random_delay = random.toLong())*//*
                                        }
                                    }
                                )
                            }
                            listFetchImages.awaitAll()    */    //
                            sendUiEvent(UiEffect.ShowToast("Fetched all users"))
                        }
                    }
                }
            }
            else -> {}
        }
    }
}

sealed class UsersEvents {
    object FetchAllUsers: UsersEvents()
}

data class UsersState(

    val users: MutableList<UserResponse> = mutableListOf(),

    val loading: Boolean = false,
    val error: String = ""
)
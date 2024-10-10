package com.example.mvi_compose.ui.coin_cryptos

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewModelScope
import com.example.mvi_compose.repositories.UserRepoImpl
import com.example.mvi_compose.network.NetworkResult
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
class UserWalletViewModel @Inject constructor(
    private val userRepo: UserRepoImpl,
    private val exchangeRatesRepo: ExchangeRatesRepoImpl
) : BaseViewModel<UserWalletState, UserWalletEvents>() {

    override fun initialState(): UserWalletState {
        return UserWalletState()
    }

//    init {
//        onEvent(UserWalletEvents.FecthUserById)
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onEvent(event: UserWalletEvents) {
        when (event) {
            is UserWalletEvents.FecthUserById -> {
                _state.update { it.copy(loading = true) }
                viewModelScope.launch(Dispatchers.IO) {
                    delay(1500)
                    when (val result = userRepo.getUserById(event.userId)) {

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
                                        user = result.data
                                    )
                                }
                            }
                            sendUiEvent(UiEffect.ShowToast("Fetched all users"))
                        }
                    }
                }
            }
            else -> {}
        }
    }
}

sealed class UserWalletEvents {
    class FecthUserById(val userId: Long): UserWalletEvents()
}

data class UserWalletState(

    val user: UserResponse = UserResponse(0, "", "", "", "", 0),
    val users: MutableList<UserResponse> = mutableListOf(),

    val loading: Boolean = false,
    val error: String = ""
)
package com.example.mvi_compose.ui.coin_cryptos

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewModelScope
import com.example.mvi_compose.repositories.UserRepoImpl
import com.example.mvi_compose.network.NetworkResult
import com.example.mvi_compose.network.data.UserResponse
import com.example.mvi_compose.network.data.WalletDetailsResponse
import com.example.mvi_compose.network.data.WalletResponse
import com.example.mvi_compose.repositories.WalletsRepoImpl
import com.example.mvi_compose.ui.BaseViewModel
import com.example.mvi_compose.ui.UiEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class UserWalletViewModel @Inject constructor(
    private val userRepo: UserRepoImpl,
    private val walletRepo: WalletsRepoImpl,
) : BaseViewModel<UserWalletState, UserWalletEvents>() {

    private val mutex = Mutex()

    override fun initialState(): UserWalletState {
        return UserWalletState()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onEvent(event: UserWalletEvents) {
        when (event) {
            is UserWalletEvents.FetchUserById -> {
                _state.update { it.copy(loading = true) }
                viewModelScope.launch(Dispatchers.IO) {
                    delay(500)
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
                                        user = result.data
                                    )
                                }
                                mutex.withLock {
                                    viewModelScope.launch {
                                        onEvent(UserWalletEvents.FetchWalletDetailsByUserId(event.userId))
                                    }
                                    viewModelScope.launch {
                                        onEvent(UserWalletEvents.FetchWalletDetails(event.userId))
                                    }
                                }
                            }
                        }
                    }
                }
            }
            is UserWalletEvents.FetchWalletDetailsByUserId -> {
                viewModelScope.launch(Dispatchers.IO) {
                    when (val result = walletRepo.getWalletInfoByUserId(event.userId.toInt())) {

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
                                        walletInfo = result.data
                                    )
                                }
                            }
                            sendUiEvent(UiEffect.ShowToast("Fetched all wallet details"))
                        }
                    }
                }
            }

            is UserWalletEvents.FetchWalletDetails -> {
                viewModelScope.launch(Dispatchers.IO) {
                    when (val result = walletRepo.getWallets(event.userId.toInt())) {

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
                                        walletList = result.data.toMutableStateList()
                                    )
                                }
                            }
                        }
                    }
                }
            }

            else -> {}
        }
    }
}

sealed class UserWalletEvents {
    class FetchUserById(val userId: Long): UserWalletEvents()
    class FetchWalletDetails(val userId: Long): UserWalletEvents()
    class FetchWalletDetailsByUserId(val userId: Long): UserWalletEvents()
}

data class UserWalletState(

    val user: UserResponse = UserResponse(0, "", "", "", "", 0),
    val walletInfo: WalletDetailsResponse = WalletDetailsResponse(listOf(), 0, WalletResponse(0,0, "", "")),

    val walletList: MutableList<WalletResponse> = mutableListOf(),

    val loading: Boolean = false,
    val error: String = ""
)
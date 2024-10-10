package com.example.mvi_compose.general.utils

import com.example.mvi_compose.di.IODispatcher
import com.example.mvi_compose.di.MovieNetwork
import com.example.mvi_compose.network.ExchangeRatesApi
import com.example.mvi_compose.repositories.UserRepo
import com.example.mvi_compose.network.UsersApi
import com.example.mvi_compose.network.WalletsApi
import com.example.mvi_compose.repositories.ExchangeRatesRepo
import com.example.mvi_compose.repositories.ExchangeRatesRepoImpl
import com.example.mvi_compose.repositories.UserRepoImpl
import com.example.mvi_compose.repositories.WalletRepo
import com.example.mvi_compose.repositories.WalletsRepoImpl
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideUsersRepository(
         usersApi: UsersApi,
         moshi: Moshi,
        @IODispatcher ioDispatcher: CoroutineDispatcher,
    ): UserRepo {
        return UserRepoImpl(usersApi, moshi, ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideExchangeRatesRepository(
        exchangeRatesApi: ExchangeRatesApi,
        moshi: Moshi,
        @IODispatcher ioDispatcher: CoroutineDispatcher,
    ): ExchangeRatesRepo {
        return ExchangeRatesRepoImpl(exchangeRatesApi, moshi, ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideWalletsRepository(
        walletsApi: WalletsApi,
        moshi: Moshi,
        @IODispatcher ioDispatcher: CoroutineDispatcher,
    ): WalletRepo {
        return WalletsRepoImpl(walletsApi, moshi, ioDispatcher)
    }
}
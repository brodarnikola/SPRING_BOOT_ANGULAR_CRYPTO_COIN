package com.example.mvi_compose.general.utils

import com.example.mvi_compose.BuildConfig
import com.example.mvi_compose.di.MovieNetwork
import com.example.mvi_compose.network.ExchangeRatesApi
import com.example.mvi_compose.network.UsersApi
import com.example.mvi_compose.network.WalletsApi
import com.example.mvi_compose.util.AppConstants
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.Date
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor().also { it.setLevel(HttpLoggingInterceptor.Level.BODY) }
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else OkHttpClient
        .Builder()
        .build()

    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .add(SerializeNulls.JSON_ADAPTER_FACTORY)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(
         okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(AppConstants.BASE_URL)
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideUsersService(retrofit: Retrofit): UsersApi = retrofit.create(UsersApi::class.java)

    @Provides
    @Singleton
    fun provideExchangeRatesService( retrofit: Retrofit): ExchangeRatesApi = retrofit.create(ExchangeRatesApi::class.java)

    @Provides
    @Singleton
    fun provideWalletsService( retrofit: Retrofit): WalletsApi = retrofit.create(WalletsApi::class.java)
}

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class SerializeNulls() {
    companion object {
        val JSON_ADAPTER_FACTORY: JsonAdapter.Factory =
            object : JsonAdapter.Factory {
//                @Nullable
                override fun create(
                    type: Type,
                    annotations: Set<Annotation>,
                    moshi: Moshi
                ): JsonAdapter<*>? {
                    val nextAnnotations =
                        Types.nextAnnotations(annotations, SerializeNulls::class.java) ?: return null
                    return moshi.nextAdapter<Any>(this, type, nextAnnotations).serializeNulls()
                }
            }
    }
}
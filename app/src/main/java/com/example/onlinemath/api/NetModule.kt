package com.example.onlinemath.api

import com.example.onlinemath.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetModule {

    @Singleton
    @Provides
    fun getHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()

        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()

        httpClient.addInterceptor(logging)

        return httpClient.build()
    }

    @Singleton
    @Provides
    fun getRetrofit(): Retrofit = Retrofit
        .Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(getHttpClient())
        .build()

    @Singleton
    @Provides
    fun getApiService(retrofit: Retrofit) = retrofit.create(ApiService::class.java)
}
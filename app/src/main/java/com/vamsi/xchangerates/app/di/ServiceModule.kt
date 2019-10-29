package com.vamsi.xchangerates.app.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.vamsi.xchangerates.app.data.remote.CurrencyService
import com.vamsi.xchangerates.app.data.remote.WorldExchangeRatesService
import com.vamsi.xchangerates.app.utils.BASE_API_LAYER
import dagger.Module
import dagger.Provides
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class ServiceModule {

    @Singleton
    @Provides
    fun provideGson(): Gson =
        GsonBuilder()
            .setLenient()
            .create()

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .certificatePinner(CertificatePinner.Builder().add(
                "apilayer.net",
                "sha256/5GptIJcpaBOtept1Bg5CKl/IIKrFnopuFQMnaNwOvKw="
            ).build())
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_API_LAYER)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()


    @Provides
    @Singleton
    fun provideCurrencyService(retrofit: Retrofit): CurrencyService =
        retrofit.create(CurrencyService::class.java)

    @Provides
    @Singleton
    fun provideWorldExchangeRatesService(retrofit: Retrofit): WorldExchangeRatesService =
        retrofit.create(WorldExchangeRatesService::class.java)
}
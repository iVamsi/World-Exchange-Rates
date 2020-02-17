package com.vamsi.xchangerates.app.di

import android.content.Context
import com.vamsi.xchangerates.app.utils.NetworkUtils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideContext(application: CurrencyApplication): Context = application.applicationContext

    @Singleton
    @Provides
    fun provideNetworkUtils(context: Context): NetworkUtils = NetworkUtils(context)

}
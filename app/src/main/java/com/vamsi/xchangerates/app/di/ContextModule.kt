package com.vamsi.xchangerates.app.di

import android.content.Context
import com.vamsi.xchangerates.app.CurrencyApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ContextModule(private val currencyApplication: CurrencyApplication) {

    @Singleton
    @Provides
    fun provideContext(): Context = currencyApplication.applicationContext

}
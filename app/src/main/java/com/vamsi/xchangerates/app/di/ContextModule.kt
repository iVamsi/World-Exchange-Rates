package com.vamsi.xchangerates.app.di

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ContextModule(private val currencyApplication: CurrencyApplication) {

    @Singleton
    @Provides
    fun provideContext(): Context = currencyApplication.applicationContext

}
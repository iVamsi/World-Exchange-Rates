package com.vamsi.xchangerates.app.di

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ContextModule() {

    @Singleton
    @Provides
    fun provideContext(application: CurrencyApplication): Context = application.applicationContext

}
package com.vamsi.xchangerates.app.core.di

import android.content.Context
import com.vamsi.xchangerates.app.data.local.CacheManager
import com.vamsi.xchangerates.app.data.local.CacheManagerImpl
import com.vamsi.xchangerates.app.data.local.WorldExchangeRatesDatabase
import com.vamsi.xchangerates.app.model.CurrencyUIModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(context: Context) = WorldExchangeRatesDatabase.buildDatabase(context)

    @Singleton
    @Provides
    fun provideCacheManager(): CacheManager<CurrencyUIModel> = CacheManagerImpl()
}
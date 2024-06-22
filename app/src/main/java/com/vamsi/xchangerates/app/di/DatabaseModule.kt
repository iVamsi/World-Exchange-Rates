package com.vamsi.xchangerates.app.di

import android.content.Context
import com.vamsi.xchangerates.app.data.local.CurrencyDao
import com.vamsi.xchangerates.app.data.local.WorldExchangeRatesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): WorldExchangeRatesDatabase {
        return WorldExchangeRatesDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideCurrencyDao(database: WorldExchangeRatesDatabase): CurrencyDao {
        return database.currencyDao()
    }
}

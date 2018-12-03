package com.vamsi.xchangerates.app.di

import android.content.Context
import com.vamsi.xchangerates.app.database.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(context: Context) = AppDatabase.buildDatabase(context)


    @Singleton
    @Provides
    fun provideCurrencyDao(appDatabase: AppDatabase) = appDatabase.currencyDao()
}
package com.vamsi.xchangerates.app.di

import android.content.Context
import com.vamsi.xchangerates.app.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(context: Context) = AppDatabase.buildDatabase(context)
}
package com.vamsi.xchangerates.app.core.di

import com.vamsi.xchangerates.app.data.repository.WorldExchangeRatesRepository
import com.vamsi.xchangerates.app.data.repository.WorldExchangeRatesRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindWorldExchangeRatesRepository(worldExchangeRatesRepository: WorldExchangeRatesRepositoryImpl): WorldExchangeRatesRepository
}
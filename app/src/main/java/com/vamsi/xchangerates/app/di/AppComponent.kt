package com.vamsi.xchangerates.app.di

import com.vamsi.xchangerates.app.CurrencyApplication
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [ContextModule::class,
        DatabaseModule::class, ServiceModule::class, ViewModule::class]
)
@Singleton
interface AppComponent {
    fun inject(currencyApplication: CurrencyApplication)
}
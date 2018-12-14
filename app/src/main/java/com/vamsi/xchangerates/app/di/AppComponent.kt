package com.vamsi.xchangerates.app.di

import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(
    modules = [AndroidSupportInjectionModule::class,
        ContextModule::class,
        DatabaseModule::class, ServiceModule::class, ViewModule::class]
)
@Singleton
interface AppComponent {
    fun inject(currencyApplication: CurrencyApplication)
}
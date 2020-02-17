package com.vamsi.xchangerates.app.core.di

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        AppModule::class,
        DatabaseModule::class,
        ServiceModule::class,
        RepositoryModule::class,
        ViewModule::class]
)
interface AppComponent : AndroidInjector<CurrencyApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<CurrencyApplication>()
}
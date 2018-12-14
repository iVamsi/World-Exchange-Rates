package com.vamsi.xchangerates.app.di

import com.vamsi.xchangerates.app.view.ui.AllCurrencies
import com.vamsi.xchangerates.app.view.ui.CurrencyConverter
import com.vamsi.xchangerates.app.view.ui.FavoriteCurrencies
import com.vamsi.xchangerates.app.view.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ViewModule {

    @ContributesAndroidInjector
    abstract fun contributesMainActivityInjector(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributesAllCurrenciesInjector(): AllCurrencies

    @ContributesAndroidInjector
    abstract fun contributesFavoriteCurrenciesInjector(): FavoriteCurrencies

    @ContributesAndroidInjector
    abstract fun contributesCurrencyConverterInjector(): CurrencyConverter

}
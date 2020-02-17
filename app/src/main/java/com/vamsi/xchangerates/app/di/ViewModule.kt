package com.vamsi.xchangerates.app.di

import com.vamsi.xchangerates.app.view.ui.AllCurrenciesFragment
import com.vamsi.xchangerates.app.view.ui.CurrencyConverterFragment
import com.vamsi.xchangerates.app.view.ui.FavoriteCurrenciesFragment
import com.vamsi.xchangerates.app.view.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ViewModule {

    @ContributesAndroidInjector
    abstract fun contributesMainActivityInjector(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributesAllCurrenciesInjector(): AllCurrenciesFragment

    @ContributesAndroidInjector
    abstract fun contributesFavoriteCurrenciesInjector(): FavoriteCurrenciesFragment

    @ContributesAndroidInjector
    abstract fun contributesCurrencyConverterInjector(): CurrencyConverterFragment

}
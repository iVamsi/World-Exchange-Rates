package com.vamsi.xchangerates.app.core.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vamsi.xchangerates.app.core.di.viewmodel.CurrencyViewModelFactory
import com.vamsi.xchangerates.app.core.di.viewmodel.ViewModelKey
import com.vamsi.xchangerates.app.view.viewmodels.AllCurrenciesViewModel
import com.vamsi.xchangerates.app.view.viewmodels.CurrencyConverterViewModel
import com.vamsi.xchangerates.app.view.viewmodels.FavoriteCurrenciesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AllCurrenciesViewModel::class)
    abstract fun bindAllCurrenciesViewModel(allCurrenciesViewModel: AllCurrenciesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteCurrenciesViewModel::class)
    abstract fun bindFavoriteCurrenciesViewModel(favoriteCurrenciesViewModel: FavoriteCurrenciesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CurrencyConverterViewModel::class)
    abstract fun bindCurrencyConverterViewModel(currencyConverterViewModel: CurrencyConverterViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: CurrencyViewModelFactory): ViewModelProvider.Factory
}
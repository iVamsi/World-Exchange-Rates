package com.vamsi.xchangerates.app.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vamsi.xchangerates.app.view.viewmodels.AllCurrenciesViewModel
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
    abstract fun bindViewModelFactory(factory: CurrencyViewModelFactory): ViewModelProvider.Factory
}
package com.vamsi.xchangerates.app.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vamsi.xchangerates.app.view.viewmodels.AllCurrenciesViewModel
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
    abstract fun bindViewModelFactory(factory: CurrencyViewModelFactory): ViewModelProvider.Factory
}
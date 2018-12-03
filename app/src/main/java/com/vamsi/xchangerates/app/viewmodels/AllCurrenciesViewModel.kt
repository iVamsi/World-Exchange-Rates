package com.vamsi.xchangerates.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vamsi.xchangerates.app.database.Currency
import com.vamsi.xchangerates.app.database.CurrencyRepository
import javax.inject.Inject

/**
 * The ViewModel for [AllCurrencies].
 */
class AllCurrenciesViewModel @Inject constructor(
    currencyRepository: CurrencyRepository
) : ViewModel() {

    private var currencyList: LiveData<List<Currency>> = MutableLiveData<List<Currency>>()

    init {
        currencyList = currencyRepository.requestUpdatedCurrencies()
    }

    fun getCurrencyList() = currencyList
}
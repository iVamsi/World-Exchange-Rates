package com.vamsi.xchangerates.app.view.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vamsi.xchangerates.app.data.repository.WorldExchangeRatesRepository
import com.vamsi.xchangerates.app.model.CurrencyUIModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class AllCurrenciesViewModel @Inject constructor(
    private val worldExchangeRatesRepository: WorldExchangeRatesRepository
) : ViewModel() {

    private var currencyList = MutableLiveData<List<CurrencyUIModel>>()

    var isLoading = ObservableField(false)

    init {
        isLoading.set(true)
        fetchCurrencies()
    }

    private fun fetchCurrencies() {
        viewModelScope.launch {
            currencyList.value = worldExchangeRatesRepository.fetchCurrencies()
            isLoading.set(false)
        }
    }

    fun getCurrencyList() = currencyList

    fun updateCurrencyFavorite(currencyId: String) {
        viewModelScope.launch {
            worldExchangeRatesRepository.updateCurrencyFavorite(currencyId)
        }
    }
}
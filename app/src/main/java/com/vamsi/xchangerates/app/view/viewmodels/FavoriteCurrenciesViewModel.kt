package com.vamsi.xchangerates.app.view.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vamsi.xchangerates.app.data.repository.WorldExchangeRatesRepository
import com.vamsi.xchangerates.app.model.CurrencyUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The ViewModel for [AllCurrencies].
 */
@HiltViewModel
class FavoriteCurrenciesViewModel @Inject constructor(
    private val worldExchangeRatesRepository: WorldExchangeRatesRepository
) : ViewModel() {

    private var currencyList = MutableLiveData<List<CurrencyUIModel>>()
    var isLoading = ObservableField(false)

    init {
        isLoading.set(true)
        fetchFavoriteCurrencies()
    }

    private fun fetchFavoriteCurrencies() {
        viewModelScope.launch {
            currencyList.value = worldExchangeRatesRepository.getFavoriteCurrencies()
            isLoading.set(false)
        }
    }

    fun getCurrencyList() = currencyList

    fun updateCurrencyFavorite(currencyId: String) {
        viewModelScope.launch {
            worldExchangeRatesRepository.updateCurrencyFavorite(currencyId, favorite = "no")
        }
    }
}
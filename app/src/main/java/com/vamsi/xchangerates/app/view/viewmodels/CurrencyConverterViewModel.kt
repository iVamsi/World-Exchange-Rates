package com.vamsi.xchangerates.app.view.viewmodels

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vamsi.xchangerates.app.BR
import com.vamsi.xchangerates.app.data.repository.WorldExchangeRatesRepository
import com.vamsi.xchangerates.app.model.CurrencyUIModel
import com.vamsi.xchangerates.app.utils.Converter
import com.vamsi.xchangerates.app.utils.ObservableViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The ViewModel for [AllCurrencies].
 */
@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(
    private val worldExchangeRatesRepository: WorldExchangeRatesRepository
) : ObservableViewModel() {

    var leftCurrencyCode: String = "EUR"
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.leftCurrencyCode)
        }

    var rightCurrencyCode: String = "INR"
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.rightCurrencyCode)
        }

    var currencyValue: String = "0"
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.currencyValue)
            notifyPropertyChanged(BR.convertedValue)
        }

    private val currencyList = MutableLiveData<List<CurrencyUIModel>>()
    private val currencies = mutableListOf<CurrencyUIModel>()

    init {
        fetchCurrencies()
    }

    private fun fetchCurrencies() {
        viewModelScope.launch {
            currencyList.value = worldExchangeRatesRepository.fetchCurrencies()
            currencies.clear()
            currencies.addAll(currencyList.value ?: emptyList())
        }
    }

    fun getCurrencyList() = currencyList

    fun onItemClick(isLeft: Boolean, currId: String) {
        if (isLeft) {
            if (leftCurrencyCode != currId) {
                leftCurrencyCode = currId
                currencyValue = "0"
            }
        } else {
            if (rightCurrencyCode != currId) {
                rightCurrencyCode = currId
                currencyValue = "0"
            }
        }
    }

    fun swapFromToCurrencies() {
        leftCurrencyCode = rightCurrencyCode.also { rightCurrencyCode = leftCurrencyCode }
        currencyValue = "0"
    }

    fun updateCurrencyValue(currValue: String) {
        currencyValue = when {
            currencyValue == "0" && currValue != "." && currValue != "C" && currValue != "back" -> currValue
            currValue == "C" -> "0"
            currValue == "." && currencyValue.contains(".") -> currencyValue
            currValue == "back" -> currencyValue.substring(0, currencyValue.length - 1)
            currencyValue.length == 6 -> currencyValue
            else -> currencyValue.plus(currValue)
        }
        if (currencyValue == "") currencyValue = "0"
    }

    @Bindable
    fun getConvertedValue(): String {
        val currValue = Converter.fromStringToDouble(currencyValue)
        return if (currValue == 0.0) "0"
        else {
            val leftCurrencyValue = getCurrencyCodeValue(currencies, leftCurrencyCode)
            val rightCurrencyValue = getCurrencyCodeValue(currencies, rightCurrencyCode)
            val converted = currValue.times(rightCurrencyValue).div(leftCurrencyValue)
            String.format("%.2f", converted)
        }
    }

    private fun getCurrencyCodeValue(list: List<CurrencyUIModel>, name: String): Double {
        return if (list.isEmpty()) 0.0
        else list.find { it.currId == name }?.currValue!!
    }
}
package com.vamsi.xchangerates.app.view.viewmodels

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import com.vamsi.xchangerates.app.BR
import com.vamsi.xchangerates.app.data.repository.CurrencyRepository
import com.vamsi.xchangerates.app.model.CurrencyUIModel
import com.vamsi.xchangerates.app.utils.Converter
import com.vamsi.xchangerates.app.utils.ObservableViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * The ViewModel for [AllCurrencies].
 */
class CurrencyConverterViewModel @Inject constructor(
    currencyRepository: CurrencyRepository
) : ObservableViewModel() {

    var leftCurrencyCode: String = "USD"
        @Bindable get() {
            return field
        }
        set(value) {
            field = value
            notifyPropertyChanged(BR.leftCurrencyCode)
        }

    var rightCurrencyCode: String = "INR"
        @Bindable get() {
            return field
        }
        set(value) {
            field = value
            notifyPropertyChanged(BR.rightCurrencyCode)
        }

    var currencyValue: String = "0"
        @Bindable get() {
            return field
        }
        set(value) {
            field = value
            notifyPropertyChanged(BR.currencyValue)
            notifyPropertyChanged(BR.convertedValue)
        }

    private var currencyList = MutableLiveData<List<CurrencyUIModel>>()
    private var currencies: List<CurrencyUIModel> = arrayListOf()
    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable.add(currencyRepository
            .getCurrenciesFromDatabase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<List<CurrencyUIModel>>() {
                override fun onError(t: Throwable) {
                    Log.e("CurrConverterViewModel", t.stackTrace.toString())
                }
                override fun onNext(data: List<CurrencyUIModel>) {
                    currencyList.value = data
                    currencies = data
                }
                override fun onComplete() {
                }
            })
        )
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
            currencyValue == "0" && currValue != "." && currValue != "C" && currValue != "back"-> currValue
            currValue == "C" -> "0"
            currValue == "." && currencyValue.contains(".") -> currencyValue
            currValue == "back" -> currencyValue.substring(0, currencyValue.length - 1)
            currencyValue.length == 6 -> currencyValue
            else -> currencyValue.plus(currValue)
        }
        if (currencyValue == "") currencyValue = "0"
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
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
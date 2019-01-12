package com.vamsi.xchangerates.app.view.viewmodels

import android.text.Editable
import android.text.TextWatcher
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

                override fun onError(e: Throwable) {
                    Log.e("CurrConverterViewModel", e.stackTrace.toString())
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

    @Bindable
    fun getCurrencyTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                currencyValue = s.toString()
            }
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

    fun updateCurrencyValue(currValue: String) {
        if (currencyValue == "0") currencyValue = currValue
        else currencyValue = currencyValue.plus(currValue)
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

    fun getCurrencyCodeValue(list: List<CurrencyUIModel>, name: String): Double {
        return if (list.isEmpty()) 0.0
        else list.find { it.currId == name }?.currValue!!
    }
}
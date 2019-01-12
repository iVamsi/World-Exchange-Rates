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

    private var currencyList = MutableLiveData<List<CurrencyUIModel>>()
    private var currencies: List<CurrencyUIModel> = arrayListOf()
    private val compositeDisposable = CompositeDisposable()

    private var currencyValue: Double = 1.0

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
                setCurrencyValue(Converter.fromStringToDouble(s.toString()))
            }
        }
    }

    fun getCurrencyList() = currencyList

    fun onItemClick(isLeft: Boolean, currId: String) {
        if (isLeft) {
            leftCurrencyCode = currId
        } else {
            rightCurrencyCode = currId
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    @Bindable
    fun getConvertedValue(): String {
        val leftCurrencyValue = getCurrencyCodeValue(currencies, leftCurrencyCode)
        val rightCurrencyValue = getCurrencyCodeValue(currencies, rightCurrencyCode)
        val converted = currencyValue.times(rightCurrencyValue).div(leftCurrencyValue)
        return converted.toString()
    }

    fun setCurrencyValue(value: Double) {
        currencyValue = value
        notifyPropertyChanged(BR.convertedValue)
    }

    fun getCurrencyCodeValue(list: List<CurrencyUIModel>, name: String): Double {
        return if (list.isEmpty()) 0.0
        else list.find { it.currId == name }?.currValue!!
    }
}
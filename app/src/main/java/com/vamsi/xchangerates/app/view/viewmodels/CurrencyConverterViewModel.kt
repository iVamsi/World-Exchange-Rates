package com.vamsi.xchangerates.app.view.viewmodels

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import com.vamsi.xchangerates.app.BR
import com.vamsi.xchangerates.app.data.repository.CurrencyRepository
import com.vamsi.xchangerates.app.model.CurrencyUIModel
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
                }

                override fun onComplete() {
                }
            })
        )
    }

    fun getCurrencyList() = currencyList

    fun onItemClick(isLeft: Boolean, currency: CurrencyUIModel) {
        if (isLeft) {
            leftCurrencyCode = currency.currId
        } else {
            rightCurrencyCode = currency.currId
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
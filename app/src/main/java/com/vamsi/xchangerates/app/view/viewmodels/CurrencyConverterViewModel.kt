package com.vamsi.xchangerates.app.view.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
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

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
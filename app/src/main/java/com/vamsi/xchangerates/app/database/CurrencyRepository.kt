package com.vamsi.xchangerates.app.database

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vamsi.xchangerates.app.service.CurrencyDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository module for handling data operations.
 */
@Singleton
class CurrencyRepository @Inject constructor(
    private val appDatabase: AppDatabase,
    private val currencyDataSource: CurrencyDataSource
) {
    val allCompositeDisposable: MutableList<Disposable> = arrayListOf()

    fun getCurrencies() = appDatabase.currencyDao().getCurrencies()

    fun getCurrency(currencyId: String) = appDatabase.currencyDao().getCurrency(currencyId)

    fun requestUpdatedCurrencies(): LiveData<List<Currency>> {
        var currencyList: LiveData<List<Currency>> = MutableLiveData<List<Currency>>()
        val disposable = currencyDataSource.requestUpdatedCurrencies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ currencyResponse ->
                if (currencyResponse.isSuccess) {
                    currencyResponse.currencyQuotes.forEach { (key, value) ->
                        AsyncTask.execute {
                            appDatabase.currencyDao().updateCurrencyValue(
                                getCurrencyIdFromResponse(key),
                                value
                            )
                        }
                    }
                    currencyList = getCurrencies()
                } else {
                    throw Throwable("CurrencyRepository -> on Error occurred")
                }
            }, { t: Throwable? -> t!!.printStackTrace() })
        allCompositeDisposable.add(disposable)
        return currencyList
    }

    private fun getCurrencyIdFromResponse(key: String) = key.substring(3)
}
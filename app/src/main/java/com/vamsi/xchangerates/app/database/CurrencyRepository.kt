package com.vamsi.xchangerates.app.database

import com.vamsi.xchangerates.app.model.CurrencyResponse
import com.vamsi.xchangerates.app.service.CurrencyDataSource
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
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

    fun getUpdatedCurrencies(): Observable<List<CurrencyDao.CurrencyUIModel>> {
        return getCurrenciesFromNetwork()
    }

    fun getCurrenciesFromNetwork(): Observable<List<CurrencyDao.CurrencyUIModel>> {
        return currencyDataSource.requestUpdatedCurrencies().flatMap {
            return@flatMap saveCurrencyResponse(it)
        }
    }

    fun saveCurrencyResponse(currencyResponse: CurrencyResponse): Observable<List<CurrencyDao.CurrencyUIModel>> {
        insertCurrencyResponse(currencyResponse)
        return getCurrenciesFromDatabase()
    }

    fun getCurrenciesFromDatabase(): Observable<List<CurrencyDao.CurrencyUIModel>> {
        return appDatabase.currencyDao().getCurrenciesForUI().toObservable()
    }

    fun insertCurrencyResponse(currencyResponse: CurrencyResponse) {
//        Schedulers.io().scheduleDirect {
            val currencyList = ArrayList<CurrencyResponseEntity>()
            currencyResponse.currencyQuotes.forEach { (currId, currValue) ->
                currencyList.add(CurrencyResponseEntity(currId.substring(3), currValue))

            }
            appDatabase.currencyDao().updateCurrencies(currencyList)
//        }
    }
}
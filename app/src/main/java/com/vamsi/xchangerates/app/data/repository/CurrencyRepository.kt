package com.vamsi.xchangerates.app.data.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.vamsi.xchangerates.app.data.local.AppDatabase
import com.vamsi.xchangerates.app.data.local.Currency
import com.vamsi.xchangerates.app.data.local.CurrencyResponseEntity
import com.vamsi.xchangerates.app.data.remote.CurrencyDataSource
import com.vamsi.xchangerates.app.model.CurrencyResponse
import com.vamsi.xchangerates.app.model.CurrencyUIModel
import com.vamsi.xchangerates.app.utils.CURRENCY_DATA_FILENAME
import com.vamsi.xchangerates.app.utils.NetworkUtils
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository @Inject constructor(
    private val appDatabase: AppDatabase,
    private val currencyDataSource: CurrencyDataSource
) {

    @Inject
    lateinit var networkUtils: NetworkUtils

    @Inject
    lateinit var context: Context

    fun getTotalCurrencies() = appDatabase.currencyDao().getCurrenciesTotal()

    fun getCurrencyFavorites(): Observable<List<CurrencyUIModel>> {
        return getCurrencyFavoritesFromDb()
    }

    fun getUpdatedCurrencies(): Observable<List<CurrencyUIModel>> {
        return getCurrenciesFromNetwork()
    }

    private fun getCurrenciesFromNetwork(): Observable<List<CurrencyUIModel>> {
        networkUtils.isConnectedToInternet?.let {
            if (it) {
                return currencyDataSource.requestUpdatedCurrencies().flatMap {
                    return@flatMap saveCurrencyResponse(it)
                }
            }
        }
        return getCurrenciesFromDatabase()
    }

    private fun saveCurrencyResponse(currencyResponse: CurrencyResponse): Observable<List<CurrencyUIModel>> {
        insertCurrencyResponse(currencyResponse)
        return getCurrenciesFromDatabase()
    }

    fun getCurrenciesFromDatabase(): Observable<List<CurrencyUIModel>> {
        return appDatabase.currencyDao().getCurrenciesForUI().toObservable()
    }

    private fun getCurrencyFavoritesFromDb(): Observable<List<CurrencyUIModel>> {
        return appDatabase.currencyDao().getCurrencyFavoritesForUI().toObservable()
    }

    fun updateCurrencyFavorite(currencyId: String, favorite: String) {
        Schedulers.io().scheduleDirect {
            appDatabase.currencyDao().updateCurrencyFavorite(currencyId, favorite)
        }
    }

    private fun insertCurrencyResponse(currencyResponse: CurrencyResponse) {
        Schedulers.io().scheduleDirect {
            val currencyList = ArrayList<CurrencyResponseEntity>()
            currencyResponse.currencyQuotes.forEach { (currId, currValue) ->
                currencyList.add(
                    CurrencyResponseEntity(
                        currId.substring(
                            3
                        ), currValue
                    )
                )
            }
            appDatabase.currencyDao().updateCurrencies(currencyList)
        }
    }

    fun initCurrencyEntitiesInDatabase() {
        val currencyEntityList = AppDatabase.getCurrencyResponseEntities()
        appDatabase.currencyDao().updateCurrencies(currencyEntityList)
    }

    fun initCurrenciesInDatabase() {

        initCurrencyEntitiesInDatabase()

        val currencyType = object : TypeToken<List<Currency>>() {}.type
        var jsonReader: JsonReader? = null

        try {
            val inputStream = context.assets.open(CURRENCY_DATA_FILENAME)
            jsonReader = JsonReader(inputStream.reader())
            val currencyList: List<Currency> = Gson().fromJson(jsonReader, currencyType)
            appDatabase.currencyDao().insertAllCurrencies(currencyList)
        } catch (ex: Exception) {
            Log.e("CurrencyRepository", "Error initializing database")
        } finally {
            jsonReader?.close()
        }
    }
}
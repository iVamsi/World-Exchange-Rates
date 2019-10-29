package com.vamsi.xchangerates.app.data.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.vamsi.xchangerates.app.BuildConfig
import com.vamsi.xchangerates.app.data.local.Currency
import com.vamsi.xchangerates.app.data.local.CurrencyResponseEntity
import com.vamsi.xchangerates.app.data.local.WorldExchangeRatesDatabase
import com.vamsi.xchangerates.app.data.remote.WorldExchangeRatesService
import com.vamsi.xchangerates.app.model.CurrencyResponse
import com.vamsi.xchangerates.app.model.CurrencyUIModel
import com.vamsi.xchangerates.app.utils.CURRENCY_DATA_FILENAME
import com.vamsi.xchangerates.app.utils.FORMAT_TYPE
import com.vamsi.xchangerates.app.utils.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WorldExchangeRatesRepositoryImpl @Inject constructor(
    private val worldExchangeRatesService: WorldExchangeRatesService,
    private val worldExchangeRatesDatabase: WorldExchangeRatesDatabase,
    private val networkUtils: NetworkUtils,
    private val context: Context,
    private val gson: Gson
) : WorldExchangeRatesRepository {

    override suspend fun getCountOfCurrenciesInDatabase(): Int {
        return withContext(Dispatchers.IO) {
            worldExchangeRatesDatabase.currencyDao().getCurrenciesTotal()
        }
    }

    override suspend fun getFavoriteCurrencies(): List<CurrencyUIModel> {
        return withContext(Dispatchers.IO) {
            worldExchangeRatesDatabase.currencyDao().getCurrencyFavoritesForUI()
        }
    }

    override suspend fun updateCurrencyFavorite(currencyId: String, favorite: String) {
        withContext(Dispatchers.IO) {
            worldExchangeRatesDatabase.currencyDao().updateCurrencyFavorite(currencyId, favorite)
        }
    }

    override suspend fun fetchCurrencies(): List<CurrencyUIModel> {
        initializeDatabase()
        networkUtils.isConnectedToInternet?.let {
            if (it) {
                val currencyResponse = worldExchangeRatesService.fetchCurrencies(
                    BuildConfig.GRADLE_CURRENCY_API_KEY,
                    FORMAT_TYPE
                )
                insertCurrencyResponse(getCurrencyListFromResponse(currencyResponse))
            }
        }
        return getCurrenciesFromDatabase()
    }

    override suspend fun insertCurrencyResponse(currencyList: List<CurrencyResponseEntity>) {
        withContext(Dispatchers.IO) {
            worldExchangeRatesDatabase.currencyDao().updateCurrencies(currencyList)
        }
    }

    private fun getCurrencyListFromResponse(currencyResponse: CurrencyResponse): List<CurrencyResponseEntity> {
        val currencyList = ArrayList<CurrencyResponseEntity>()
        currencyResponse.currencyQuotes.forEach { (currId, currValue) ->
            currencyList.add(CurrencyResponseEntity(currId, currValue))
        }
        return currencyList
    }

    override suspend fun getCurrenciesFromDatabase(): List<CurrencyUIModel> {
        return withContext(Dispatchers.IO) {
            worldExchangeRatesDatabase.currencyDao().getCurrenciesForUI()
        }
    }

    private suspend fun initCurrencyEntitiesInDatabase() {
        val currencyEntityList = WorldExchangeRatesDatabase.getCurrencyResponseEntities()
        insertCurrencyResponse(currencyEntityList)
    }

    override suspend fun initializeDatabase() {

        withContext(Dispatchers.IO) {
            if (getCountOfCurrenciesInDatabase() == 0) {

                initCurrencyEntitiesInDatabase()

                val currencyType = object : TypeToken<List<Currency>>() {}.type
                var jsonReader: JsonReader? = null

                try {
                    val inputStream = context.assets.open(CURRENCY_DATA_FILENAME)
                    jsonReader = JsonReader(inputStream.reader())
                    val currencyList: List<Currency> = gson.fromJson(jsonReader, currencyType)
                    worldExchangeRatesDatabase.currencyDao().insertAllCurrencies(currencyList)
                } catch (ex: Exception) {
                    Log.e("CurrencyRepository", "Error initializing database")
                } finally {
                    jsonReader?.close()
                }
            }
        }
    }
}
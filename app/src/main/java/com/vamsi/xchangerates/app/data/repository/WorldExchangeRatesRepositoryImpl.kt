package com.vamsi.xchangerates.app.data.repository

import com.vamsi.xchangerates.app.BuildConfig
import com.vamsi.xchangerates.app.data.local.CurrencyDao
import com.vamsi.xchangerates.app.data.local.CurrencyResponseEntity
import com.vamsi.xchangerates.app.data.local.WorldExchangeRatesDatabase
import com.vamsi.xchangerates.app.data.remote.CurrencyApi
import com.vamsi.xchangerates.app.di.IoDispatcher
import com.vamsi.xchangerates.app.model.CurrencyLocal
import com.vamsi.xchangerates.app.model.CurrencyResponse
import com.vamsi.xchangerates.app.model.CurrencyUIModel
import com.vamsi.xchangerates.app.model.toCurrency
import com.vamsi.xchangerates.app.utils.Constants.CURRENCY_DATA_FILENAME
import com.vamsi.xchangerates.app.utils.Constants.FORMAT_TYPE
import com.vamsi.xchangerates.app.utils.ContextUtil
import com.vamsi.xchangerates.app.utils.NetworkUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class WorldExchangeRatesRepositoryImpl @Inject constructor(
    private val currencyApi: CurrencyApi,
    private val networkUtils: NetworkUtils,
    private val contextUtil: ContextUtil,
    private val currencyDao: CurrencyDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : WorldExchangeRatesRepository {

    override suspend fun getCountOfCurrenciesInDatabase(): Int {
        return withContext(ioDispatcher) {
            currencyDao.getCurrenciesTotal()
        }
    }

    override suspend fun getFavoriteCurrencies(): List<CurrencyUIModel> {
        return withContext(ioDispatcher) {
            currencyDao.getCurrencyFavoritesForUI()
        }
    }

    override suspend fun updateCurrencyFavorite(currencyId: String, favorite: String) {
        withContext(ioDispatcher) {
            currencyDao.updateCurrencyFavorite(currencyId, favorite)
        }
    }

    override suspend fun fetchCurrencies(): List<CurrencyUIModel> {
        initializeDatabase()
        networkUtils.isConnectedToInternet?.let {
            if (it) {
                val currencyResponse = currencyApi.fetchCurrencies(
                    BuildConfig.CURRENCY_API_KEY,
                    FORMAT_TYPE
                )
                insertCurrencyResponse(getCurrencyListFromResponse(currencyResponse))
            }
        }
        return getCurrenciesFromDatabase()
    }

    override suspend fun insertCurrencyResponse(currencyList: List<CurrencyResponseEntity>) {
        withContext(ioDispatcher) {
            currencyDao.updateCurrencies(currencyList)
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
        return withContext(ioDispatcher) {
            currencyDao.getCurrenciesForUI()
        }
    }

    private suspend fun initCurrencyEntitiesInDatabase() {
        val currencyEntityList = WorldExchangeRatesDatabase.getCurrencyResponseEntities()
        insertCurrencyResponse(currencyEntityList)
    }

    override suspend fun initializeDatabase() {

        withContext(ioDispatcher) {
            if (getCountOfCurrenciesInDatabase() == 0) {

                initCurrencyEntitiesInDatabase()

                val json = Json { ignoreUnknownKeys = true }

                contextUtil.getContext().assets.open(CURRENCY_DATA_FILENAME).use { inputStream ->
                    val currencyList: List<CurrencyLocal> = json.decodeFromString(inputStream.bufferedReader().use { it.readText() })
                    currencyDao.insertAllCurrencies(currencyList.map { it.toCurrency() })
                }
            }
        }
    }
}
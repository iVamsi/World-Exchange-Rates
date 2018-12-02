package com.vamsi.xchangerates.app.database

/**
 * Repository module for handling data operations.
 */
class CurrencyRepository private constructor(private val currencyDao: CurrencyDao) {

    fun getCurrencies() = currencyDao.getCurrencies()

    fun getCurrency(currencyId: String) = currencyDao.getCurrency(currencyId)

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: CurrencyRepository? = null

        fun getInstance(currencyDao: CurrencyDao) =
            instance ?: synchronized(this) {
                instance ?: CurrencyRepository(currencyDao).also { instance = it }
            }
    }
}
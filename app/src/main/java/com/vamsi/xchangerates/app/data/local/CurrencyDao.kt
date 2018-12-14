package com.vamsi.xchangerates.app.data.local

import androidx.room.*
import com.vamsi.xchangerates.app.model.CurrencyUIModel
import io.reactivex.Flowable

/**
 * The Data Access Object for the Currency class.
 */
@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currencies ORDER BY currencyId")
    fun getCurrencies(): Flowable<List<Currency>>

    @Query("SELECT * FROM currencies WHERE id = :currencyId")
    fun getCurrency(currencyId: String): Flowable<Currency>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(currencies: List<CurrencyResponseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCurrencies(currencies: List<Currency>)

    @Query("UPDATE currencies SET currencyValue = :currencyValue WHERE currencyId = :currencyId")
    fun updateCurrencyValue(currencyId: String, currencyValue: Double)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll(currencyList: List<Currency>)

    @Transaction
    fun updateCurrencies(currencies: List<CurrencyResponseEntity>) {
        deleteAllCurrencies()
        insertAll(currencies)
    }

    @Query("DELETE FROM currencyresponse")
    fun deleteAllCurrencies()

    @Query(
        "SELECT currencies.currencyId AS currId, currencies.currencyName AS currName, currencyresponse.currencyValue AS currValue " +
                "FROM currencies, currencyresponse " +
                "WHERE currencies.currencyId = currencyresponse.currencyId"
    )
    fun getCurrenciesForUI(): Flowable<List<CurrencyUIModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrencyResponse(currencyResponseEntity: CurrencyResponseEntity)

}
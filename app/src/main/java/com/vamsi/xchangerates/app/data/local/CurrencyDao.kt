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

    @Query("SELECT COUNT(*) FROM currencies")
    fun getCurrenciesTotal(): Flowable<Int>

    @Query("SELECT * FROM currencies WHERE id = :currencyId")
    fun getCurrency(currencyId: String): Flowable<Currency>

    @Query("SELECT * FROM currencies WHERE currencyFavorite = 'yes'")
    fun getFavorites(): Flowable<List<Currency>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(currencies: List<CurrencyResponseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllInCurrencyTable(currencies: List<Currency>)

    @Transaction
    fun insertAllCurrencies(currencies: List<Currency>) {
        insertAllInCurrencyTable(currencies)
    }

    @Query("UPDATE currencies SET currencyValue = :currencyValue WHERE currencyId = :currencyId")
    fun updateCurrencyValue(currencyId: String, currencyValue: Double)

    @Query("UPDATE currencies SET currencyFavorite = :favorite WHERE currencyId = :currencyId")
    fun updateCurrencyFavorite(currencyId: String, favorite: String)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll(currencyList: List<Currency>)

    @Transaction
    fun updateCurrencies(currencies: List<CurrencyResponseEntity>) {
        deleteAllCurrencies()
        insertAll(currencies)
    }

    @Query("DELETE FROM currencyresponse")
    fun deleteAllCurrencies()

    @Query("""
        SELECT currencies.currencyId AS currId, currencies.currencyName AS currName, currencyresponse.currencyValue AS currValue
                FROM currencies, currencyresponse
                WHERE currencies.currencyId = currencyresponse.currencyId
    """
    )
    fun getCurrenciesForUI(): Flowable<List<CurrencyUIModel>>

    @Query("""
        SELECT currencies.currencyId AS currId, currencies.currencyName AS currName, currencyresponse.currencyValue AS currValue
                FROM currencies, currencyresponse
                WHERE currencies.currencyId = currencyresponse.currencyId
                AND currencies.currencyFavorite = 'yes'
    """
    )
    fun getCurrencyFavoritesForUI(): Flowable<List<CurrencyUIModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrencyResponse(currencyResponseEntity: CurrencyResponseEntity)

}
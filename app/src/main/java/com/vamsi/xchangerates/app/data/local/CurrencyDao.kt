package com.vamsi.xchangerates.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.vamsi.xchangerates.app.model.CurrencyUIModel

/**
 * The Data Access Object for the Currency class.
 */
@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currencies ORDER BY currencyId")
    suspend fun getCurrencies(): List<Currency>

    @Query("SELECT COUNT(*) FROM currencies")
    suspend fun getCurrenciesTotal(): Int

    @Query("SELECT * FROM currencies WHERE id = :currencyId")
    suspend fun getCurrency(currencyId: String): Currency

    @Query("SELECT * FROM currencies WHERE currencyFavorite = 'yes'")
    suspend fun getFavorites(): List<Currency>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(currencies: List<CurrencyResponseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllInCurrencyTable(currencies: List<Currency>)

    @Transaction
    suspend fun insertAllCurrencies(currencies: List<Currency>) {
        insertAllInCurrencyTable(currencies)
    }

    @Query("UPDATE currencies SET currencyValue = :currencyValue WHERE currencyId = :currencyId")
    suspend fun updateCurrencyValue(currencyId: String, currencyValue: Double)

    @Query("UPDATE currencies SET currencyFavorite = :favorite WHERE currencyId = :currencyId")
    suspend fun updateCurrencyFavorite(currencyId: String, favorite: String)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAll(currencyList: List<Currency>)

    @Transaction
    suspend fun updateCurrencies(currencies: List<CurrencyResponseEntity>) {
        deleteAllCurrencies()
        insertAll(currencies)
    }

    @Query("DELETE FROM currencyresponse")
    suspend fun deleteAllCurrencies()

    @Query("""
        SELECT currencies.currencyId AS currId, currencies.currencyName AS currName, currencyresponse.currencyValue AS currValue
                FROM currencies, currencyresponse
                WHERE currencies.currencyId = currencyresponse.currencyId
    """
    )
    suspend fun getCurrenciesForUI(): List<CurrencyUIModel>

    @Query("""
        SELECT currencies.currencyId AS currId, currencies.currencyName AS currName, currencyresponse.currencyValue AS currValue
                FROM currencies, currencyresponse
                WHERE currencies.currencyId = currencyresponse.currencyId
                AND currencies.currencyFavorite = 'yes'
    """
    )
    suspend fun getCurrencyFavoritesForUI(): List<CurrencyUIModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencyResponse(currencyResponseEntity: CurrencyResponseEntity)
}
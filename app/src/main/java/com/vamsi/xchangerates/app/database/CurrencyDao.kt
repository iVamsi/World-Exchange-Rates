package com.vamsi.xchangerates.app.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * The Data Access Object for the Currency class.
 */
@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currencies ORDER BY currencyId")
    fun getCurrencies(): LiveData<List<Currency>>

    @Query("SELECT * FROM currencies WHERE id = :currencyId")
    fun getCurrency(currencyId: String): LiveData<Currency>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(plants: List<Currency>)

    @Query("UPDATE currencies SET currencyValue = :currencyValue WHERE currencyId = :currencyId")
    fun updateCurrencyValue(currencyId: String, currencyValue: Double): Int
}
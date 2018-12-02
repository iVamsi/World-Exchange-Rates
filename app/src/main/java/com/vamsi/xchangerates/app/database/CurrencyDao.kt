package com.vamsi.xchangerates.app.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable

/**
 * The Data Access Object for the CurrencyEntity class.
 */
@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currencies ORDER BY currencyId")
    fun getCurrencies(): Flowable<List<CurrencyEntity>>

    @Query("SELECT * FROM currencies WHERE id = :currencyId")
    fun getCurrency(currencyId: String): Flowable<CurrencyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(plants: List<CurrencyEntity>)
}
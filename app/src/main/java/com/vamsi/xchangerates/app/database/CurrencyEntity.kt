package com.vamsi.xchangerates.app.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vamsi.xchangerates.app.utils.TABLE_CURRENCIES

@Entity(tableName = TABLE_CURRENCIES)
data class CurrencyEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    var currencyId: String,
    var currencyName: String,
    var currencyValue: Double,
    var currencyFavorite: String,
    var baseCurrency: String
)
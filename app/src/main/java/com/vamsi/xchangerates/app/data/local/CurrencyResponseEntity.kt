package com.vamsi.xchangerates.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vamsi.xchangerates.app.utils.TABLE_CURRENCY_RESPONSE

@Entity(tableName = TABLE_CURRENCY_RESPONSE)
data class CurrencyResponseEntity(
    @PrimaryKey
    var currencyId: String,
    var currencyValue: Double
)

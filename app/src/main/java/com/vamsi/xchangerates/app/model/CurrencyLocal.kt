package com.vamsi.xchangerates.app.model

import com.vamsi.xchangerates.app.data.local.Currency
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CurrencyLocal(
    val currencyId: String,
    val currencyName: String,
    val currencyValue: Double,
    val currencyFavorite: String,
    val baseCurrency: String
)

fun CurrencyLocal.toCurrency() = Currency(
    id = UUID.randomUUID().mostSignificantBits,
    currencyId = currencyId,
    currencyName = currencyName,
    currencyValue = currencyValue,
    currencyFavorite = currencyFavorite,
    baseCurrency = baseCurrency
)

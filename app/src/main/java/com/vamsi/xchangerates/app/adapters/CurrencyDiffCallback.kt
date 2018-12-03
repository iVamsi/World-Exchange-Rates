package com.vamsi.xchangerates.app.adapters

import androidx.recyclerview.widget.DiffUtil
import com.vamsi.xchangerates.app.database.Currency

class CurrencyDiffCallback : DiffUtil.ItemCallback<Currency>() {

    override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean {
        return oldItem.currencyId == newItem.currencyId
    }

    override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean {
        return oldItem == newItem
    }
}
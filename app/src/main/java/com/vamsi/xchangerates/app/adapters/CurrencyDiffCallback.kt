package com.vamsi.xchangerates.app.adapters

import androidx.recyclerview.widget.DiffUtil
import com.vamsi.xchangerates.app.database.CurrencyDao

class CurrencyDiffCallback : DiffUtil.ItemCallback<CurrencyDao.CurrencyUIModel>() {

    override fun areItemsTheSame(oldItem: CurrencyDao.CurrencyUIModel, newItem: CurrencyDao.CurrencyUIModel): Boolean {
        return oldItem.currId == newItem.currId
    }

    override fun areContentsTheSame(oldItem: CurrencyDao.CurrencyUIModel, newItem: CurrencyDao.CurrencyUIModel): Boolean {
        return oldItem == newItem
    }
}
package com.vamsi.xchangerates.app.view.adapters

import androidx.recyclerview.widget.DiffUtil
import com.vamsi.xchangerates.app.model.CurrencyUIModel

class CurrencyDiffCallback : DiffUtil.ItemCallback<CurrencyUIModel>() {

    override fun areItemsTheSame(oldItem: CurrencyUIModel, newItem: CurrencyUIModel): Boolean {
        return oldItem.currId == newItem.currId
    }

    override fun areContentsTheSame(oldItem: CurrencyUIModel, newItem: CurrencyUIModel): Boolean {
        return oldItem == newItem
    }
}
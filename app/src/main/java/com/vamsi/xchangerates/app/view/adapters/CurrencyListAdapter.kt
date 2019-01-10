package com.vamsi.xchangerates.app.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vamsi.xchangerates.app.databinding.DialogListItemCurrencyBinding
import com.vamsi.xchangerates.app.model.CurrencyUIModel
import com.vamsi.xchangerates.app.utils.OnClickHandler

/**
 * Adapter for the [RecyclerView] in [CurrencyConverter] fragment.
 */
class CurrencyListAdapter(var listener: OnClickHandler) : ListAdapter<CurrencyUIModel, CurrencyListAdapter.ViewHolder>(CurrencyDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currency = getItem(position)
        holder.apply {
            bind(listener, currency)
            itemView.tag = currency
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DialogListItemCurrencyBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    class ViewHolder(
        private val binding: DialogListItemCurrencyBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: OnClickHandler, item: CurrencyUIModel) {
            binding.apply {
                clickListener = listener
                currency = item
                executePendingBindings()
            }
        }
    }
}
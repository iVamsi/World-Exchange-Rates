package com.vamsi.xchangerates.app.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vamsi.xchangerates.app.databinding.DialogListItemCurrencyBinding
import com.vamsi.xchangerates.app.model.CurrencyUIModel

/**
 * Adapter for the [RecyclerView] in [CurrencyConverter] fragment.
 */
class CurrencyListAdapter : ListAdapter<CurrencyUIModel, CurrencyListAdapter.ViewHolder>(CurrencyDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currency = getItem(position)
        holder.apply {
            bind(createOnClickListener(currency.currId), currency)
            itemView.tag = currency
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DialogListItemCurrencyBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    private fun createOnClickListener(currencyId: String): View.OnClickListener {
        return View.OnClickListener {
            println("Row clicked")
        }
    }

    class ViewHolder(
        private val binding: DialogListItemCurrencyBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, item: CurrencyUIModel) {
            binding.apply {
                clickListener = listener
                currency = item
                executePendingBindings()
            }
        }
    }
}
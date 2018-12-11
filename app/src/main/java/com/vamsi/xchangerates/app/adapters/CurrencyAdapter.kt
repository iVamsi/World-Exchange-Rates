package com.vamsi.xchangerates.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vamsi.xchangerates.app.database.CurrencyDao
import com.vamsi.xchangerates.app.databinding.ListItemCurrencyBinding

/**
 * Adapter for the [RecyclerView] in [AllCurrencies] fragment.
 */
class CurrencyAdapter : ListAdapter<CurrencyDao.CurrencyUIModel, CurrencyAdapter.ViewHolder>(CurrencyDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currency = getItem(position)
        holder.apply {
            bind(createOnClickListener(currency.currId), currency)
            itemView.tag = currency
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemCurrencyBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    private fun createOnClickListener(plantId: String): View.OnClickListener {
        return View.OnClickListener {
            println("Row clicked")
        }
    }

    class ViewHolder(
        private val binding: ListItemCurrencyBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, item: CurrencyDao.CurrencyUIModel) {
            binding.apply {
                clickListener = listener
                currency = item
                executePendingBindings()
            }
        }
    }
}
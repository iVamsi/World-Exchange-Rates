package com.vamsi.xchangerates.app.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.vamsi.xchangerates.app.R
import com.vamsi.xchangerates.app.databinding.FragmentFavoriteCurrenciesBinding
import com.vamsi.xchangerates.app.model.CurrencyUIModel
import com.vamsi.xchangerates.app.utils.OnClickHandler
import com.vamsi.xchangerates.app.utils.autoCleared
import com.vamsi.xchangerates.app.utils.observe
import com.vamsi.xchangerates.app.utils.viewModelProvider
import com.vamsi.xchangerates.app.view.adapters.CurrencyAdapter
import com.vamsi.xchangerates.app.view.viewmodels.FavoriteCurrenciesViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class FavoriteCurrenciesFragment : DaggerFragment(), OnClickHandler {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var favoriteCurrenciesViewModel: FavoriteCurrenciesViewModel
    private lateinit var adapter: CurrencyAdapter

    var binding by autoCleared<FragmentFavoriteCurrenciesBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<FragmentFavoriteCurrenciesBinding>(
            inflater,
            R.layout.fragment_favorite_currencies,
            container,
            false
        ).apply {
            binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        favoriteCurrenciesViewModel = viewModelProvider(viewModelFactory)
        adapter = CurrencyAdapter(this)
        binding.apply {
            viewModel = favoriteCurrenciesViewModel
            executePendingBindings()
            currencyList.addItemDecoration(
                DividerItemDecoration(
                    context!!,
                    DividerItemDecoration.VERTICAL
                )
            )
            currencyList.adapter = adapter
        }
        observe(favoriteCurrenciesViewModel.getCurrencyList(), ::onCurrencyListAvailable)
    }

    private fun onCurrencyListAvailable(currencyList: List<CurrencyUIModel>?) {
        currencyList?.let {
            adapter.submitList(it)
        }
    }

    override fun onClick(view: View) { }

    override fun onItemLongClick(currencyId: String): Boolean {
        val dialog = AlertDialog.Builder(context!!)
            .setMessage(R.string.remove_favorite)
            .setPositiveButton(R.string.button_yes) {
                    _, _ -> favoriteCurrenciesViewModel.updateCurrencyFavorite(currencyId)
            }
            .setNegativeButton(R.string.button_no) {
                    dialog, _ -> dialog.dismiss()
            }
            .create()
        dialog.show()
        return true
    }

    override fun onItemClicked(currencyId: String) { }
}
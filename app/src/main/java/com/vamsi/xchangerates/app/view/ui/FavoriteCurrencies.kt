package com.vamsi.xchangerates.app.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.vamsi.xchangerates.app.R
import com.vamsi.xchangerates.app.databinding.FragmentFavoriteCurrenciesBinding
import com.vamsi.xchangerates.app.utils.autoCleared
import com.vamsi.xchangerates.app.view.adapters.CurrencyAdapter
import com.vamsi.xchangerates.app.view.viewmodels.FavoriteCurrenciesViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class FavoriteCurrencies : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var favoriteCurrenciesViewModel: FavoriteCurrenciesViewModel

    var binding by autoCleared<FragmentFavoriteCurrenciesBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentFavoriteCurrenciesBinding>(
            inflater,
            R.layout.fragment_favorite_currencies,
            container,
            false
        )

        binding = dataBinding
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        favoriteCurrenciesViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(FavoriteCurrenciesViewModel::class.java)
        val adapter = CurrencyAdapter()
        binding.viewModel = favoriteCurrenciesViewModel
        binding.executePendingBindings()

        binding.currencyList.addItemDecoration(
            DividerItemDecoration(
                context!!,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.currencyList.adapter = adapter
        subscribeUi(adapter)
    }

    private fun subscribeUi(adapter: CurrencyAdapter) {
        favoriteCurrenciesViewModel.getCurrencyList().observe(viewLifecycleOwner, Observer { currencyList ->
            if (currencyList != null) adapter.submitList(currencyList)
        })
    }
}
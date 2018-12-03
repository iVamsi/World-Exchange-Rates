package com.vamsi.xchangerates.app.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.vamsi.xchangerates.app.R
import com.vamsi.xchangerates.app.adapters.CurrencyAdapter
import com.vamsi.xchangerates.app.databinding.FragmentAllCurrenciesBinding
import com.vamsi.xchangerates.app.utils.autoCleared
import com.vamsi.xchangerates.app.viewmodels.AllCurrenciesViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class AllCurrencies : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var allCurrenciesViewModel: AllCurrenciesViewModel

    var binding by autoCleared<FragmentAllCurrenciesBinding>()

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val dataBinding = DataBindingUtil.inflate<FragmentAllCurrenciesBinding>(
            inflater,
            R.layout.fragment_all_currencies,
            container,
            false
        )

        binding = dataBinding
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        allCurrenciesViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(AllCurrenciesViewModel::class.java)
        val adapter = CurrencyAdapter()
        binding.currencyList.adapter = adapter
        subscribeUi(adapter)
    }

    private fun subscribeUi(adapter: CurrencyAdapter) {
        allCurrenciesViewModel.getCurrencyList().observe(viewLifecycleOwner, Observer { currencyList ->
            if (currencyList != null) adapter.submitList(currencyList)
        })
    }
}
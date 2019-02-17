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
import com.vamsi.xchangerates.app.databinding.FragmentAllCurrenciesBinding
import com.vamsi.xchangerates.app.utils.OnClickHandler
import com.vamsi.xchangerates.app.utils.autoCleared
import com.vamsi.xchangerates.app.utils.viewModelProvider
import com.vamsi.xchangerates.app.view.adapters.CurrencyAdapter
import com.vamsi.xchangerates.app.view.viewmodels.AllCurrenciesViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class AllCurrenciesFragment : DaggerFragment(), OnClickHandler {
    override fun onClick(view: View) {}

    override fun onItemLongClick(currencyId: String): Boolean {
        val dialog = AlertDialog.Builder(context!!)
            .setMessage(R.string.set_favorite)
            .setPositiveButton(R.string.button_yes) {
                    _, _ -> allCurrenciesViewModel.updateCurrencyFavorite(currencyId)
            }
            .setNegativeButton(R.string.button_no) {
                    dialog, _ -> dialog.dismiss()
            }
            .create()
        dialog.show()
        return true
    }

    override fun onItemClicked(currencyId: String) {}

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var allCurrenciesViewModel: AllCurrenciesViewModel

    var binding by autoCleared<FragmentAllCurrenciesBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentAllCurrenciesBinding>(
            inflater,
            com.vamsi.xchangerates.app.R.layout.fragment_all_currencies,
            container,
            false
        )

        binding = dataBinding
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        allCurrenciesViewModel = viewModelProvider(viewModelFactory)
        val adapter = CurrencyAdapter(this)
        binding.apply {
            viewModel = allCurrenciesViewModel
            executePendingBindings()
            currencyList.addItemDecoration(
                DividerItemDecoration(
                    context!!,
                    DividerItemDecoration.VERTICAL
                )
            )
            currencyList.adapter = adapter
        }
        subscribeUi(adapter)
    }

    private fun subscribeUi(adapter: CurrencyAdapter) {
        allCurrenciesViewModel.getCurrencyList().observe(viewLifecycleOwner, Observer { currencyList ->
            if (currencyList != null) adapter.submitList(currencyList)
        })
    }
}
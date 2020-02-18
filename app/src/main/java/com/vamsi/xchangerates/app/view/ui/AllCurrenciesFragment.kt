package com.vamsi.xchangerates.app.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.vamsi.xchangerates.app.R
import com.vamsi.xchangerates.app.core.exception.Failure
import com.vamsi.xchangerates.app.core.extension.autoCleared
import com.vamsi.xchangerates.app.core.extension.observe
import com.vamsi.xchangerates.app.core.extension.viewModelProvider
import com.vamsi.xchangerates.app.databinding.FragmentAllCurrenciesBinding
import com.vamsi.xchangerates.app.model.CurrencyUIModel
import com.vamsi.xchangerates.app.utils.OnClickHandler
import com.vamsi.xchangerates.app.view.adapters.CurrencyAdapter
import com.vamsi.xchangerates.app.view.viewmodels.AllCurrenciesViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class AllCurrenciesFragment : DaggerFragment(), OnClickHandler {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var allCurrenciesViewModel: AllCurrenciesViewModel
    private lateinit var adapter: CurrencyAdapter

    var binding by autoCleared<FragmentAllCurrenciesBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<FragmentAllCurrenciesBinding>(
            inflater,
            R.layout.fragment_all_currencies,
            container,
            false
        ).apply {
            binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        allCurrenciesViewModel = viewModelProvider(viewModelFactory)
        adapter = CurrencyAdapter(this)
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
        observe(allCurrenciesViewModel.getCurrencyList(), ::onCurrencyListAvailable)
        observe(allCurrenciesViewModel.failure, ::handleFailure)
    }

    private fun handleFailure(failure: Failure?) {
        when (failure) {
            is Failure.NetworkConnection -> renderFailure(R.string.failure_network_connection)
            is Failure.ServerError -> renderFailure(R.string.failure_server_error)
        }
    }

    private fun renderFailure(@StringRes message: Int) {
        Snackbar.make(binding.mainLayout, message, Snackbar.LENGTH_LONG).show()
    }

    private fun onCurrencyListAvailable(currencyList: List<CurrencyUIModel>?) {
        currencyList?.let {
            adapter.submitList(it)
        }
    }

    override fun onClick(view: View) {}

    override fun onItemLongClick(currencyId: String): Boolean {
        AlertDialog.Builder(context!!)
            .setMessage(R.string.set_favorite)
            .setPositiveButton(R.string.button_yes) {
                    _, _ -> allCurrenciesViewModel.updateCurrencyFavorite(currencyId)
            }
            .setNegativeButton(R.string.button_no) {
                    dialog, _ -> dialog.dismiss()
            }
            .create()
            .show()
        return true
    }

    override fun onItemClicked(currencyId: String) {}
}
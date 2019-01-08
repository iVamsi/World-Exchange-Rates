package com.vamsi.xchangerates.app.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.vamsi.xchangerates.app.databinding.FragmentCurrencyConverterBinding
import com.vamsi.xchangerates.app.utils.OnClickHandler
import com.vamsi.xchangerates.app.utils.autoCleared
import com.vamsi.xchangerates.app.view.viewmodels.CurrencyConverterViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.vamsi.xchangerates.app.R
import com.vamsi.xchangerates.app.R.*
import com.vamsi.xchangerates.app.model.CurrencyUIModel
import com.vamsi.xchangerates.app.view.adapters.CurrencyListAdapter

class CurrencyConverter : DaggerFragment(), OnClickHandler {
    override fun onClick(view: View) {
        when (view.id) {
            R.id.leftCurrencyLayout -> showCurrencyListDialog()
            R.id.rightCurrencyLayout -> showCurrencyListDialog()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var currencyConverterViewModel: CurrencyConverterViewModel

    var binding by autoCleared<FragmentCurrencyConverterBinding>()

    lateinit var currencyUIModel: List<CurrencyUIModel>

    lateinit var alertDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentCurrencyConverterBinding>(
            inflater,
            layout.fragment_currency_converter,
            container,
            false
        )

        binding = dataBinding
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        currencyConverterViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CurrencyConverterViewModel::class.java)

        binding.converterTopSection.clickHandler = this
        val adapter = CurrencyListAdapter()
        initCurrencyListDialog(adapter)
        subscribeUi(adapter)
    }

    private fun subscribeUi(adapter: CurrencyListAdapter) {
        currencyConverterViewModel.getCurrencyList()
            .observe(viewLifecycleOwner, Observer { currencyList ->
                if (currencyList != null) {
                    currencyUIModel = currencyList
                    adapter.submitList(currencyList)
                }
            })
    }

    fun initCurrencyListDialog(adapter: CurrencyListAdapter) {
        val builder = AlertDialog.Builder(context!!)
        val dialogView = layoutInflater.inflate(
            R.layout.currency_list_layout,
            null
        ) as View
        builder.setView(dialogView)
        val rv =
            dialogView.findViewById<View>(R.id.rv_currency_list) as RecyclerView
        rv.addItemDecoration(
            DividerItemDecoration(
                context!!,
                DividerItemDecoration.VERTICAL
            )
        )
        rv.adapter = adapter
        alertDialog = builder.create()
    }

    private fun showCurrencyListDialog() {
        alertDialog.show()
    }
}
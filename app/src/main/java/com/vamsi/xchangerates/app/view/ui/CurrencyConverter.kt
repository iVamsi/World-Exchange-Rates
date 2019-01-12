package com.vamsi.xchangerates.app.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.vamsi.xchangerates.app.R
import com.vamsi.xchangerates.app.databinding.FragmentCurrencyConverterBinding
import com.vamsi.xchangerates.app.model.CurrencyUIModel
import com.vamsi.xchangerates.app.utils.OnClickHandler
import com.vamsi.xchangerates.app.utils.autoCleared
import com.vamsi.xchangerates.app.view.adapters.CurrencyListAdapter
import com.vamsi.xchangerates.app.view.viewmodels.CurrencyConverterViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class CurrencyConverter : DaggerFragment(), OnClickHandler {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var currencyConverterViewModel: CurrencyConverterViewModel
    var binding by autoCleared<FragmentCurrencyConverterBinding>()
    lateinit var currencyUIModel: List<CurrencyUIModel>
    lateinit var alertDialog: AlertDialog
    lateinit var currency: CurrencyUIModel

    var isLeftCurrencyClicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentCurrencyConverterBinding>(
            inflater,
            com.vamsi.xchangerates.app.R.layout.fragment_currency_converter,
            container,
            false
        )

        binding = dataBinding
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        currencyConverterViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CurrencyConverterViewModel::class.java)
        binding.converterTopSection.viewModel = currencyConverterViewModel
        binding.converterBottomSection.clickHandler = this
        binding.converterTopSection.clickHandler = this
        val adapter = CurrencyListAdapter(this)
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
            com.vamsi.xchangerates.app.R.layout.currency_list_layout,
            null
        ) as View
        builder.setView(dialogView)
        val rv =
            dialogView.findViewById<View>(com.vamsi.xchangerates.app.R.id.rv_currency_list) as RecyclerView
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

    override fun onItemClicked(currencyId: String) {
        alertDialog.dismiss()
        currencyConverterViewModel.onItemClick(isLeftCurrencyClicked, currencyId)
    }

    override fun onClick(view: View) {
        if (view.tag != null && view.tag == getString(R.string.number_button)) {
            val enteredNumber = (view as TextView).text.toString()
            currencyConverterViewModel.updateCurrencyValue(enteredNumber)
            return
        }
        when (view.id) {
            com.vamsi.xchangerates.app.R.id.leftCurrencyLayout -> {
                isLeftCurrencyClicked = true
                showCurrencyListDialog()
            }
            com.vamsi.xchangerates.app.R.id.rightCurrencyLayout -> {
                isLeftCurrencyClicked = false
                showCurrencyListDialog()
            }
        }
    }

}
package com.vamsi.xchangerates.app.view.ui

import android.content.Intent
import android.os.Build
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

class CurrencyConverterFragment : DaggerFragment(), OnClickHandler {
    override fun onItemLongClick(currencyId: String): Boolean {
        return true
    }

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
            R.layout.fragment_currency_converter,
            container,
            false
        )

        binding = dataBinding
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        currencyConverterViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CurrencyConverterViewModel::class.java)
        binding.apply {
            converterTopSection.viewModel = currencyConverterViewModel
            converterBottomSection.clickHandler = this@CurrencyConverterFragment
            converterTopSection.clickHandler = this@CurrencyConverterFragment
        }
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
            R.layout.currency_list_layout,
            null
        ) as View
        builder.setView(dialogView)
        val rv = dialogView.findViewById<View>(R.id.rv_currency_list) as RecyclerView
        rv.apply {
            addItemDecoration(
                DividerItemDecoration(
                    context!!,
                    DividerItemDecoration.VERTICAL
                )
            )
            this.adapter = adapter
        }
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
            updateCurrencyValue(view)
            return
        }
        when (view.id) {
            R.id.leftCurrencyLayout -> {
                isLeftCurrencyClicked = true
                showCurrencyListDialog()
            }
            R.id.rightCurrencyLayout -> {
                isLeftCurrencyClicked = false
                showCurrencyListDialog()
            }
            R.id.t9_key_clear -> {
                updateCurrencyValue(view)
            }
            R.id.t9_key_backspace -> {
                currencyConverterViewModel.updateCurrencyValue(getString(R.string.btn_backspace))
            }
            R.id.converterImage -> {
                currencyConverterViewModel.swapFromToCurrencies()
            }
            R.id.t9_key_share -> {
                shareConversion()
            }
        }
    }

    private fun updateCurrencyValue(view: View) {
        val enteredNumber = (view as TextView).text.toString()
        currencyConverterViewModel.updateCurrencyValue(enteredNumber)
    }

    private fun shareConversion() {
        val fromValue = binding.converterTopSection.leftCurrencyValue.text.toString()
        val fromCurrency = binding.converterTopSection.leftCurrencyId.text.toString()
        val toValue = binding.converterTopSection.rightCurrencyValue.text.toString()
        val toCurrency = binding.converterTopSection.rightCurrencyId.text.toString()

        val shareBody = ( fromValue + " " + fromCurrency
                + " = " + toValue + " " + toCurrency
                + "\n" + getString(R.string.shared_using)
                + " " + getString(R.string.app_name))
        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sharingIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT)
        }
        sharingIntent.putExtra(
            Intent.EXTRA_SUBJECT,
            resources.getString(R.string.currency_conversion)
        )
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(sharingIntent, resources.getString(R.string.share_via)))
    }
}
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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.vamsi.xchangerates.app.R
import com.vamsi.xchangerates.app.databinding.FragmentCurrencyConverterBinding
import com.vamsi.xchangerates.app.model.CurrencyUIModel
import com.vamsi.xchangerates.app.utils.OnClickHandler
import com.vamsi.xchangerates.app.utils.autoCleared
import com.vamsi.xchangerates.app.utils.observe
import com.vamsi.xchangerates.app.utils.viewModelProvider
import com.vamsi.xchangerates.app.view.adapters.CurrencyListAdapter
import com.vamsi.xchangerates.app.view.viewmodels.CurrencyConverterViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class CurrencyConverterFragment : DaggerFragment(), OnClickHandler {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var binding by autoCleared<FragmentCurrencyConverterBinding>()
    lateinit var currency: CurrencyUIModel
    private lateinit var adapter: CurrencyListAdapter

    private var isLeftCurrencyClicked = false
    private lateinit var currencyConverterViewModel: CurrencyConverterViewModel
    private lateinit var alertDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<FragmentCurrencyConverterBinding>(
            inflater,
            R.layout.fragment_currency_converter,
            container,
            false
        ).apply {
            binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        currencyConverterViewModel = viewModelProvider(viewModelFactory)
        binding.apply {
            converterTopSection.viewModel = currencyConverterViewModel
            converterBottomSection.clickHandler = this@CurrencyConverterFragment
            converterTopSection.clickHandler = this@CurrencyConverterFragment
        }
        adapter = CurrencyListAdapter(this)
        initCurrencyListDialog(adapter)
        observe(currencyConverterViewModel.getCurrencyList(), ::onCurrencyListAvailable)
    }

    private fun onCurrencyListAvailable(currencyList: List<CurrencyUIModel>?) {
        currencyList?.let {
            adapter.submitList(it)
        }
    }

    private fun initCurrencyListDialog(adapter: CurrencyListAdapter) {
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

    override fun onItemLongClick(currencyId: String): Boolean {
        return true
    }

    override fun onClick(view: View) {
        view.tag?.let {
            if (view.tag == getString(R.string.number_button)) {
                updateCurrencyValue(view)
                return
            }
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
        val sharingIntent = Intent(Intent.ACTION_SEND)
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
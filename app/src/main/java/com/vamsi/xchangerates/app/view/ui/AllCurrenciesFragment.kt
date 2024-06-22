package com.vamsi.xchangerates.app.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.vamsi.xchangerates.app.R
import com.vamsi.xchangerates.app.databinding.FragmentAllCurrenciesBinding
import com.vamsi.xchangerates.app.model.CurrencyUIModel
import com.vamsi.xchangerates.app.utils.OnClickHandler
import com.vamsi.xchangerates.app.utils.observe
import com.vamsi.xchangerates.app.view.adapters.CurrencyAdapter
import com.vamsi.xchangerates.app.view.viewmodels.AllCurrenciesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllCurrenciesFragment : Fragment(), OnClickHandler {

    private val allCurrenciesViewModel: AllCurrenciesViewModel by viewModels()
    private lateinit var adapter: CurrencyAdapter

    private var _binding: FragmentAllCurrenciesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return DataBindingUtil.inflate<FragmentAllCurrenciesBinding>(
            inflater,
            R.layout.fragment_all_currencies,
            container,
            false
        ).apply {
            _binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = CurrencyAdapter(this)
        binding.apply {
            viewModel = allCurrenciesViewModel
            executePendingBindings()
            currencyList.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            currencyList.adapter = adapter
        }
        observe(allCurrenciesViewModel.getCurrencyList(), ::onCurrencyListAvailable)
    }

    private fun onCurrencyListAvailable(currencyList: List<CurrencyUIModel>?) {
        currencyList?.let {
            adapter.submitList(it)
        }
    }

    override fun onClick(view: View) {}

    override fun onItemLongClick(currencyId: String): Boolean {
        AlertDialog.Builder(requireContext())
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

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
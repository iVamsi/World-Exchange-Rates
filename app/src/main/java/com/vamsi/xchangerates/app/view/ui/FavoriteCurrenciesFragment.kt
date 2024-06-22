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
import com.vamsi.xchangerates.app.databinding.FragmentFavoriteCurrenciesBinding
import com.vamsi.xchangerates.app.model.CurrencyUIModel
import com.vamsi.xchangerates.app.utils.OnClickHandler
import com.vamsi.xchangerates.app.utils.observe
import com.vamsi.xchangerates.app.view.adapters.CurrencyAdapter
import com.vamsi.xchangerates.app.view.viewmodels.FavoriteCurrenciesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteCurrenciesFragment : Fragment(), OnClickHandler {

    private val favoriteCurrenciesViewModel: FavoriteCurrenciesViewModel by viewModels()
    private lateinit var adapter: CurrencyAdapter

    private var _binding: FragmentFavoriteCurrenciesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return DataBindingUtil.inflate<FragmentFavoriteCurrenciesBinding>(
            inflater,
            R.layout.fragment_favorite_currencies,
            container,
            false
        ).apply {
            _binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = CurrencyAdapter(this)
        binding.apply {
            viewModel = favoriteCurrenciesViewModel
            executePendingBindings()
            currencyList.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
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
        val dialog = AlertDialog.Builder(requireContext())
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

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}

package com.vamsi.xchangerates.app.domain

import com.vamsi.xchangerates.app.core.interactor.UseCase
import com.vamsi.xchangerates.app.core.interactor.UseCase.None
import com.vamsi.xchangerates.app.data.repository.WorldExchangeRatesRepository
import com.vamsi.xchangerates.app.model.CurrencyUIModel
import javax.inject.Inject

class GetCurrencies
@Inject constructor(private val worldExchangeRatesRepository: WorldExchangeRatesRepository) : UseCase<List<CurrencyUIModel>, None>() {

    override suspend fun run(params: None) = worldExchangeRatesRepository.fetchCurrencies()
}
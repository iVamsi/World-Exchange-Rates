package com.vamsi.xchangerates.app.data.local

import android.util.Log
import com.vamsi.xchangerates.app.model.CurrencyUIModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CacheManagerImpl @Inject constructor() : CacheManager<CurrencyUIModel> {

    private val data: ArrayList<CurrencyUIModel> = arrayListOf()

    override fun getData(): Flow<List<CurrencyUIModel>> {
        return flow {
            if (data.isNotEmpty()) {
                Log.d("CacheManagerImpl", "From cache")
                emit(data)
            }
        }
    }

    override fun saveDataInMemory(data: List<CurrencyUIModel>) {
        Log.d("CacheManagerImpl", "Save to cache")
        this.data.addAll(data)
    }
}

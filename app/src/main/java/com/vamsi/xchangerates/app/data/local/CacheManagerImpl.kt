package com.vamsi.xchangerates.app.data.local

import android.util.Log
import com.vamsi.xchangerates.app.model.CurrencyUIModel
import io.reactivex.Observable
import javax.inject.Inject

class CacheManagerImpl @Inject constructor() : CacheManager<CurrencyUIModel> {

    private val data: ArrayList<CurrencyUIModel> = arrayListOf()

    override fun getData(): Observable<List<CurrencyUIModel>> {
        return Observable.create<List<CurrencyUIModel>> { emitter ->
            if (data.isNotEmpty()) {
                Log.d("CacheManagerImpl", "From cache")
                emitter.onNext(data)
            }
            emitter.onComplete()
        }
    }

    override fun saveDataInMemory(data: List<CurrencyUIModel>) {
        Log.d("CacheManagerImpl", "Save to cache")
        this.data.addAll(data)
    }
}
package com.vamsi.xchangerates.app.data.local

import io.reactivex.Observable

interface CacheManager<T> {
    fun getData(): Observable<List<T>>
    fun saveDataInMemory(data: List<T>)
}
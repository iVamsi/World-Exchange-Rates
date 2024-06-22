package com.vamsi.xchangerates.app.data.local

import kotlinx.coroutines.flow.Flow

interface CacheManager<T> {
    fun getData(): Flow<List<T>>
    fun saveDataInMemory(data: List<T>)
}

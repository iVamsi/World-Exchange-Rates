package com.vamsi.xchangerates.app.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.vamsi.xchangerates.app.database.AppDatabase
import com.vamsi.xchangerates.app.database.CurrencyEntity
import com.vamsi.xchangerates.app.utils.CURRENCY_DATA_FILENAME

class CurrencyDatabaseWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    private val TAG by lazy { CurrencyDatabaseWorker::class.java.simpleName }

    override fun doWork(): Result {
        val currencyType = object : TypeToken<List<CurrencyEntity>>() {}.type
        var jsonReader: JsonReader? = null

        return try {
            val inputStream = applicationContext.assets.open(CURRENCY_DATA_FILENAME)
            jsonReader = JsonReader(inputStream.reader())
            val currencyList: List<CurrencyEntity> = Gson().fromJson(jsonReader, currencyType)
            val database = AppDatabase.getInstance(applicationContext)
            database.currencyDao().insertAll(currencyList)
            Result.SUCCESS
        } catch (ex: Exception) {
            Log.e(TAG, "Error populating database", ex)
            Result.FAILURE
        } finally {
            jsonReader?.close()
        }
    }
}
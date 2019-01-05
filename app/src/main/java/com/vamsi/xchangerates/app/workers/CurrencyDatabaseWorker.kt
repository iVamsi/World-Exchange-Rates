package com.vamsi.xchangerates.app.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.vamsi.xchangerates.app.data.local.AppDatabase
import com.vamsi.xchangerates.app.data.local.Currency
import com.vamsi.xchangerates.app.utils.CURRENCY_DATA_FILENAME
import javax.inject.Inject

class CurrencyDatabaseWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    private val TAG by lazy { CurrencyDatabaseWorker::class.java.simpleName }

    @Inject
    lateinit var database: AppDatabase

    override fun doWork(): Result {
        val currencyType = object : TypeToken<List<Currency>>() {}.type
        var jsonReader: JsonReader? = null

        return try {
            val inputStream = applicationContext.assets.open(CURRENCY_DATA_FILENAME)
            jsonReader = JsonReader(inputStream.reader())
            val currencyList: List<Currency> = Gson().fromJson(jsonReader, currencyType)
            val db = AppDatabase.getInstance(applicationContext)
            db.currencyDao().insertAllCurrencies(currencyList)
            Result.success()
        } catch (ex: Exception) {
            Log.e(TAG, "Error populating database", ex)
            Result.failure()
        } finally {
            jsonReader?.close()
        }
    }
}
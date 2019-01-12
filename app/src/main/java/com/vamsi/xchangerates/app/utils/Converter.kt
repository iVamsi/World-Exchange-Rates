package com.vamsi.xchangerates.app.utils

object Converter {
    fun fromStringToDouble(str: String): Double {
        // Remove letters and other characters
        val filteredValue = str.replace(Regex("""[^\d:.]"""), "")
        if (filteredValue.isEmpty()) return 0.0
        return filteredValue.toDouble()
    }
}
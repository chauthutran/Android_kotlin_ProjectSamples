package com.psi.fhir.utils

import java.text.SimpleDateFormat
import java.util.Date

object DateUtils {

    fun convertMillisToDate(millis: Long, formatPattern: String): String {
        val formatter = SimpleDateFormat(formatPattern)
        return formatter.format(Date(millis))
    }

    fun getCurrentDate(): String {
        return Date().toLocaleString()
    }
}
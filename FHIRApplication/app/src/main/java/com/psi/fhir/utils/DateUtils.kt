package com.psi.fhir.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Date

object DateUtils {

    fun convertMillisToDate(millis: Long, formatPattern: String): String {
        val formatter = SimpleDateFormat(formatPattern)
        return formatter.format(Date(millis))
    }

    fun getCurrentDate(): String {
        return Date().toLocaleString()
    }

    fun getAge(date: LocalDate?): Int {
        if( date == null ) return 0

        return Period.between(
            LocalDate.of(date.year, date.month, date.dayOfMonth),
            LocalDate.now()
        ).years
    }

    fun formatDate(date: LocalDate?, formatPattern: String?): String {
        if( date == null) return ""
        val pattern = if( formatPattern == null ) "dd-MM-yyyy" else formatPattern

        // Define a pattern for formatting the LocalDate
        val formatter = DateTimeFormatter.ofPattern(pattern)

        // Format the LocalDate using the formatter
        return date.format(formatter)
    }
}


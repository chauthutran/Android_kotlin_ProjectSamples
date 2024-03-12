package com.psi.onlineshop.utils

import java.text.SimpleDateFormat
import java.util.Date

fun getCurrentDateStr() : String {
    val currentDate = Date()
    return "${currentDate.year}-${currentDate.month }-${currentDate.date}"
}
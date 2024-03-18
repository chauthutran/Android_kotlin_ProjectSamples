package com.psi.onlineshop.utils

import android.health.connect.datatypes.units.Percentage
import java.text.SimpleDateFormat
import java.util.Date

fun getCurrentDateStr() : String {
    val currentDate = Date()
    return "${currentDate.toLocaleString()}"
}

fun Float.getOfferPercentagePrice( offerPercentage: Float ): Float {
    return this * (1f - offerPercentage)
}


fun Float.getPercentage(): Float {
    return (this * 100)
}

fun Float.formatNumber(): String {
   return "${String.format("%.2f", this)}"
}
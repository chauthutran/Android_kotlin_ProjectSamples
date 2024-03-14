package com.psi.onlineshop.data

import kotlinx.serialization.Serializable
import org.json.JSONObject

@Serializable
data class Product (
    val _id: String? = null,
    val name: String = "",
    val category: String = "",
    val price: Float = 0f,
    val offerPercentage: Float? = null,
    val description: String? = null,
    val colors: List<Int>? = null,
    val sizes: List<String>? = null,
    var imgFileIds: ArrayList<String> = ArrayList(),
): java.io.Serializable
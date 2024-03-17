package com.psi.onlineshop.data

import com.psi.onlineshop.utils.getCurrentDateStr
import kotlinx.serialization.Serializable
import org.json.JSONObject

@Serializable
data class Product (
    val _id: String? = null,
    val name: String = "",
    val description: String? = null,
    val category: String = "",
    var variants: ArrayList<ProductVariant> = ArrayList(),
    val date: String = getCurrentDateStr()
): java.io.Serializable

@Serializable
data class ProductVariant (
    val price: Float = 0f,
    val offerPercentage: Float? = null,
    val stockQuantity: Int = 0,
    var imageName: String? = null,
    val color: Int? = 0,
    val size: String? = null
): java.io.Serializable


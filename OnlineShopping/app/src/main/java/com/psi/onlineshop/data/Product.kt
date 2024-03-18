package com.psi.onlineshop.data

import com.psi.onlineshop.utils.getCurrentDateStr
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Product (
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val description: String? = null,
    val category: String = "",
    var variants: ArrayList<ProductVariant> = ArrayList(),
    val createdDate: String = getCurrentDateStr(),
    val lastUpdatedDate: String = getCurrentDateStr()
): java.io.Serializable // java.io.Serializable --> Use to transfer product data between fragments

@Serializable
data class ProductVariant (
    val price: Float = 0f,
    val offerPercentage: Float? = null,
    val stockQuantity: Int = 0,
    var imageName: String? = null,
    val color: Int? = 0,
    val size: String? = null
): java.io.Serializable


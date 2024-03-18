package com.psi.onlineshop.data

import com.psi.onlineshop.utils.getCurrentDateStr
import kotlinx.serialization.Serializable

@Serializable
data class Order (
    val _id: String? = null,
    val userId: String = "",
    var status: String = "",
    val paymentMethod: String = "",
    var shippingAddress: ShippingAddress?  = null,
    val createdDate: String = getCurrentDateStr(),
    val items: ArrayList<OrderItem> = ArrayList()

): java.io.Serializable


@Serializable
data class OrderItem (
    val productId: String = "",
    val quantity: Int = 0
): java.io.Serializable
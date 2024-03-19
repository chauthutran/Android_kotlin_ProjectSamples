package com.psi.onlineshop.data

import com.psi.onlineshop.utils.getCurrentDateStr
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String? = null,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val isCustomer: Boolean = true,
    val imagePath: String = "",
    val createdDate: String = getCurrentDateStr(),
    val lastUpdatedDate: String = getCurrentDateStr(),
    val shippingAddresses: ArrayList<ShippingAddress> = ArrayList()
)

@Serializable
data class ShippingAddress(
    var id: String = "",
    val addressTitle: String = "",
    val fullName: String = "",
    val street: String = "",
    val phone: String = "",
    val city: String = "",
    val state: String = ""
): java.io.Serializable

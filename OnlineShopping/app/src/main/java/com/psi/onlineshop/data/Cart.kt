package com.psi.onlineshop.data

import com.psi.onlineshop.utils.getCurrentDateStr
import kotlinx.serialization.Serializable

@Serializable
class Cart (
    val _id: String? = null,
    val userId: String = "",
    val productId: String = "",
    val quantity: Int = 0,
    val createdDate: String = getCurrentDateStr()
): java.io.Serializable
package com.psi.onlineshop.data

import com.psi.onlineshop.utils.getCurrentDateStr
import kotlinx.serialization.Serializable

@Serializable
class ProductLike (
    val _id: String? = null,
    val userId: String = "",
    val productId: String = "",
    val createdDate: String = getCurrentDateStr()
): java.io.Serializable
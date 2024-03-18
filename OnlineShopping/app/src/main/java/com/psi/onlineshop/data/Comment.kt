package com.psi.onlineshop.data

import com.psi.onlineshop.utils.getCurrentDateStr
import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val _id: String? = null,
    val userId: String = "",
    val productId: String = "",
    val commentText: String = "",
    val createdDate: String = getCurrentDateStr()
): java.io.Serializable

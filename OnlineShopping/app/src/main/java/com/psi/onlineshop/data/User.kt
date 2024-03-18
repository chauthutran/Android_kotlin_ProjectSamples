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
    val lastUpdatedDate: String = getCurrentDateStr()
)

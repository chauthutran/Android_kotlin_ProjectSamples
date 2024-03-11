package com.psi.onlineshop.data

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val _id: String? = null,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val isCustomer: Boolean = true,
    val imagePath: String = "",
)

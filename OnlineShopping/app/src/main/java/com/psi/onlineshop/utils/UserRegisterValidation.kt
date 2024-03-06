package com.psi.onlineshop.utils

sealed class UserRegisterValidation() {
    object Success: UserRegisterValidation()
    data class Failed( val message: String): UserRegisterValidation()
}

data class UserRegisterFieldsState (
    val email: UserRegisterValidation,
    val password: UserRegisterValidation
)
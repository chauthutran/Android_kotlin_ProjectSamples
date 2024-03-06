package com.psi.onlineshop.utils

import android.util.Patterns


fun validateEmail(email: String): UserRegisterValidation {
    if( email.isEmpty())
        return UserRegisterValidation.Failed("Email is required")
    if( !Patterns.EMAIL_ADDRESS.matcher(email).matches() )
        return UserRegisterValidation.Failed("Wrong email format")

    return UserRegisterValidation.Success
}

fun validatePassword(password: String): UserRegisterValidation {
    if( password.isEmpty() )
        return UserRegisterValidation.Failed("Password is required")
    if( password.length < 6 )
        return UserRegisterValidation.Failed("Password should contains 6 chars")

    return UserRegisterValidation.Success
}
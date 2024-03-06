package com.psi.onlineshop.fragments.loginregister

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.psi.onlineshop.R

public class RegisterFragmentDirections private constructor() {
  public companion object {
    public fun actionRegisterFragmentToLoginFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_registerFragment_to_loginFragment)
  }
}

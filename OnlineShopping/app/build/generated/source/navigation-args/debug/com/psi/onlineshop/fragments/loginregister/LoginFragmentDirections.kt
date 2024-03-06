package com.psi.onlineshop.fragments.loginregister

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.psi.onlineshop.R

public class LoginFragmentDirections private constructor() {
  public companion object {
    public fun actionLoginFragmentToRegisterFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_loginFragment_to_registerFragment)
  }
}

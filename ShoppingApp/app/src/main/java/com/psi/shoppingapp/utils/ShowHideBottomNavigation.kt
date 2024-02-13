package com.psi.shoppingapp.utils

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.psi.shoppingapp.R
import com.psi.shoppingapp.activities.ShoppingActivity

fun Fragment.hideBottomNavigationView() {
    // Hide the bottom navigation
    val bottomNavigationView = ( activity as ShoppingActivity).findViewById<BottomNavigationView>(
        R.id.bottomNavigation
    )
    bottomNavigationView.visibility = View.GONE
}

fun Fragment.showBottomNavigationView() {
    // Hide the bottom navigation
    val bottomNavigationView = ( activity as ShoppingActivity).findViewById<BottomNavigationView>(
        R.id.bottomNavigation
    )
    bottomNavigationView.visibility = View.VISIBLE
}
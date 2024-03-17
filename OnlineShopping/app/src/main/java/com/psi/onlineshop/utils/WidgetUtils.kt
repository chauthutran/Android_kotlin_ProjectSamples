package com.psi.onlineshop.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.psi.onlineshop.R
import com.psi.onlineshop.activities.ShoppingActivity


fun RecyclerView.getItem(itemFieldId: Int, value: String ): View? {
    val views = this.children
    for( view in views) {
        val textView = view.findViewById<TextView>(itemFieldId)
        textView?.let {
            if( textView.text.toString() == value ) return view
        }
    }

    return null
}

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
package com.psi.shoppingapp.data.order

import com.psi.shoppingapp.data.Address
import com.psi.shoppingapp.data.CartProduct

data class Order(
    val orderStatus: String,
    val totalPrice: Float,
    val products: List<CartProduct>,
    val address: Address
) {
}
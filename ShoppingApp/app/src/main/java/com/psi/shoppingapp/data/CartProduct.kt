package com.psi.shoppingapp.data

data class CartProduct(
    var product: Product,
    val quantity: Int,
    val selectedColor: Int? = null,
    val selectedSize: String? = null
) {
    constructor(): this( Product(), 1, null, null )
}

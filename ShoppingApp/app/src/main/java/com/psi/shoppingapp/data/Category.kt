package com.psi.shoppingapp.data

sealed class Category( val category: String ) {
    object Chair: Category("Chair")
    object Furniture: Category("Furniture")
    object Accessory: Category("Accessory")
}

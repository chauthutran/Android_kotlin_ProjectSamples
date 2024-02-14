package com.psi.shoppingapp.helper

fun Float?.getProductPrice(price: Float): Float {
    if( this == null )
        return price

    val remainingPricePercentage = 1f - this
    var priceAfterOffer = price * remainingPricePercentage

    return priceAfterOffer
}
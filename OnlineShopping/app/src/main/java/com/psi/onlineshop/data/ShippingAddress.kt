package com.psi.onlineshop.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
data class ShippingAddress(
    var id: String = "",
    val addressTitle: String = "",
    val fullName: String = "",
    val street: String = "",
    val phone: String = "",
    val city: String = "",
    val state: String = ""
): java.io.Serializable

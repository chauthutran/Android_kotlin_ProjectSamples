package com.psi.onlineshop.data

import com.psi.onlineshop.utils.getCurrentDateStr
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
class ProductLike (
    val id: String = UUID.randomUUID().toString(),
    val userId: String = "",
    val productId:  String = "",
    val createdDate: String = getCurrentDateStr()
): java.io.Serializable
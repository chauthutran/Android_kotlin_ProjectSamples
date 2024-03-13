package com.psi.onlineshop.data

import kotlinx.serialization.Serializable
import org.json.JSONObject

@Serializable
data class Product (
    val _id: String? = null,
    val name: String = "",
    val category: String = "",
    val price: Float = 0f,
    val offerPercentage: Float? = null,
    val description: String? = null,
    val colors: List<Int>? = null,
    val sizes: List<String>? = null,
    var imgFileIds: ArrayList<String> = ArrayList(),
) {

    var images = ArrayList<String>()
        get() = field

    fun setImageList( imageMap: JSONObject) {
        images = ArrayList<String>()
        var imgFileIds = this.imgFileIds

        (0 until imgFileIds.size).forEach {
            val fileId = imgFileIds.get(it)
            images.add(imageMap.getJSONObject(fileId).getString("data"))
        }
    }



}
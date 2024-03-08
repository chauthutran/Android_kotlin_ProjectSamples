package com.psi.onlineshop.data

import com.google.gson.Gson
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

//import io.realm.kotlin.types.RealmObject
//import io.realm.kotlin.types.annotations.PrimaryKey
//import org.bson.codecs.pojo.annotations.BsonId
//import org.bson.types.ObjectId

@Serializable data class User(

//    @PrimaryKey
//    var _id: ObjectId = ObjectId()

    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val isCustomer: Boolean = true,
    val imagePath: String = "",
)

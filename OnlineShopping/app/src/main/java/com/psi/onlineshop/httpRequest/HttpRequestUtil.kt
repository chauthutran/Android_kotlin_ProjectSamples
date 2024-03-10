package com.psi.onlineshop.httpRequest

import com.google.gson.Gson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject

class HttpRequestUtil {
    companion object {
        fun convertObjToJson(obj: Any): JSONObject {
            var gson = Gson()
            var jsonString = gson.toJson(obj)
            return JSONObject(jsonString)
        }

//        inline fun <reified T> convertJsonToObj(responseJson: Any) : T {
//            var json = Json { ignoreUnknownKeys = true }
//            return json.decodeFromString(responseJson.toString())
//        }

    }


}
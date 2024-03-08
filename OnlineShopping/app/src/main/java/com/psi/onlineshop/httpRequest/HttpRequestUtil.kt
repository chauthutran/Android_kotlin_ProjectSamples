package com.psi.onlineshop.httpRequest

import com.google.gson.Gson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject

class HttpRequestUtil {
    companion object {
        fun convertObjToJsonStr(obj: Any): String {
            var gson = Gson()
            return gson.toJson(obj)
        }

//        inline fun <reified T> convertJsonToObj(responseJson: Any)  where T : T {
//            var json = Json { ignoreUnknownKeys = true }
//
////            var gson = Gson()
////            return gson.toJson(obj)
//
//            return json.decodeFromString(responseJson.toString())
//        }
        inline fun <reified T> convertJsonToObj(responseJson: Any) : T {
            var json = Json { ignoreUnknownKeys = true }
            return json.decodeFromString(responseJson.toString())
        }

    }


}
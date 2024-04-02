package com.psi.fhir.utils

import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.util.regex.Matcher




object JSONUtils {

    fun toJSONObject(jsonString: String): JSONObject? {
        if( jsonString.isNullOrEmpty() ) {
            return null
        }

        return JSONObject(jsonString)
    }


    // Convert a data object to JSONObject
    fun convertObjToJson(obj: Any): JSONObject {
        var gson = Gson()
        var jsonString = gson.toJson(obj)
        return JSONObject(jsonString)
    }

    fun cloneJsonArray(originalJsonArray: JSONArray): JSONArray {
        val clonedJsonArray = JSONArray(originalJsonArray.toString())
        return clonedJsonArray
    }

    fun cloneJsonObject(originalJson: JSONObject): JSONObject {
        val clonedJson = JSONObject(originalJson.toString())
        return clonedJson
    }

   fun parseJSONValue(data: JSONObject, sourceJson: JSONObject): JSONObject {

       val precessingJson = JSONUtils.cloneJsonObject(sourceJson);
       val resultJSON = JSONObject()
       val keys = precessingJson.keys()

       // Iterate through your keys

       // Iterate through your keys
       while (keys.hasNext()) {
           val key = keys.next()
           var value = precessingJson[key]
           if (value is Int) {
                //
           } else if (value is String) {
               val expression = "var data = ${data}; ${value};"
               println("======== parseJSONValue: ${expression}")
               precessingJson.put(key, evaluateJavaScript(expression))
           } else if (value is JSONObject) {
                   // Your nested JSONObjects
                   // Recursively call this method
                   value = JSONUtils.parseJSONValue(value, JSONObject())
           }

           // Add to your new JSON Object
           resultJSON.put(key, value)
       }

        return resultJSON

    }
}
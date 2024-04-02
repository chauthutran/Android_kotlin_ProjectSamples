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

   fun parseJSONValue(data: JSONObject, sourceJson: JSONObject, resultJSON: JSONObject): JSONObject {

       val precessingJson = cloneJsonObject(sourceJson);
       val keys = precessingJson.keys()
       try {

           // Iterate through your keys
           while (keys.hasNext()) {
               val key = keys.next()
               var value = precessingJson[key]
               if (value is Int) {
                   //
               } else if (value is String) {
                   val expression = "var data = ${data}; ${value};"
                   value = evaluateJavaScript(expression)
               } else if (value is JSONObject) {
                   // Recursively call this method
                   value = parseJSONValue(data, value, JSONObject())
               }
               else if (value is JSONArray) {
                   // Recursively call this method
                   var list = value as JSONArray
                   for (i in 0 until list.length()) {
                       value = list.get(i)
                       if (value is String) {
                           val expression = "var data = ${data}; ${value};"
                           value = evaluateJavaScript(expression)
                       }
                       else
                       {
                           value = parseJSONValue(data, list.get(i) as JSONObject , JSONObject())
                       }
                   }
               }

               // Add to your new JSON Object
               resultJSON.put(key, value)
           }

       }
       catch( ex: Exception )
       {
           println(" ============= ex: ${ex.message}")
       }

       return resultJSON

    }
}
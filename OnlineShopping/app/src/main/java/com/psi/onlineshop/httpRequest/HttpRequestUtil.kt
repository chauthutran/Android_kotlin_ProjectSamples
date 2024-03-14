package com.psi.onlineshop.httpRequest

import android.R
import android.app.Application
import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.UUID


class HttpRequestUtil {

    companion object {

        fun convertObjToJson(obj: Any): JSONObject {
            var gson = Gson()
            var jsonString = gson.toJson(obj)
            return JSONObject(jsonString)
        }

        inline fun sendPOSTRequest(context: Context, actionName: String, collectionName: String, payloadJson: JSONObject, crossinline completion: (JSONObject) -> Unit) {

            try {
                // Make new json object and put params in it

                var jsonParams = JSONObject()
                jsonParams.put("payload", payloadJson)
                jsonParams.put("collectionName", collectionName)

                // Building a request
                val request = JsonObjectRequest(
                    Request.Method.POST,  // Using a variable for the domain is great for testing
                    "${HttpRequestConfig.BASE_URL_MONGODB_SERVICE}?action=${actionName}",
                    jsonParams,
                    {
                        // Handle the response
                        completion(it)
                    }
                ) {
                    // Handle the error
                    var errJson = JSONObject()
                    errJson.put("status", HttpRequestConfig.RESPONSE_STATUS_ERROR)
                    errJson.put("data", it.message)

                    completion( errJson )
                }


                Volley.newRequestQueue(context).add<JSONObject>(request)

            } catch (ex: JSONException) {
                // Catch if something went wrong with the params
                var errJson = JSONObject()
                errJson.put("status", HttpRequestConfig.RESPONSE_STATUS_ERROR)
                errJson.put("data", ex.message)

                completion( errJson )
            }

        }

        inline fun uploadImage(context: Context, imageData: ByteArray, crossinline completion: (JSONObject) -> Unit ) {
            val queue = Volley.newRequestQueue(context)
            val url = "${HttpRequestConfig.BASE_URL_MONGODB_SERVICE}/uploadFileToStorage"

            val stringRequest = object : VolleyFileUploadRequest(
                Method.POST,
                url,
                Response.Listener { response ->
                    completion( JSONObject(String(response.data)) )
                },
                Response.ErrorListener {
                    var errJson = JSONObject()
                    errJson.put("status", HttpRequestConfig.RESPONSE_STATUS_ERROR)
                    errJson.put("data", it.message)
                    completion( errJson )
                }
            ) {

                override fun getByteData(): MutableMap<String, FileDataPart> {
                    var params = HashMap<String, FileDataPart>()
                    println("imagName: img-${UUID.randomUUID()}.jpg")
                    params.put("uploadfile", FileDataPart("img-${UUID.randomUUID()}.jpg", imageData, "jpg"))
                    return params
                }
            }
            queue.add(stringRequest)
        }

        inline fun <reified T> convertJsonToObj(data: JSONObject) : T {
            var json = Json { ignoreUnknownKeys = true }
            return json.decodeFromString<T>(data.toString())
        }

        inline fun <reified T> convertJsonArrToListObj(data: JSONArray) : List<T> {
            var result = ArrayList<T>()
            try {
                var json = Json { ignoreUnknownKeys = true }
                for (i in 0 until data.length()) { // for (i in 0 until list.length()) {
                    result.add( json.decodeFromString<T>(data.getJSONObject(i).toString()) )
                    // Your code here
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
            finally {
                return result
            }
        }

    }


}
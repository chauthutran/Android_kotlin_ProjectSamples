package com.psi.onlineshop.httpRequest


import com.google.gson.Gson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.concurrent.Executors


/**
 *
 * ********* Get ***********************************************************************************
 * val request = HttpRequest(
 *     url = "https://httpbin.org/get",
 *     parameters = mapOf("name" to "Alex")
 * )
 * request.json<HttpBin> { result, response ->
 *     println(result)
 * }
 *
 * ********* Post **********************************************************************************
 * val request = HttpRequest(
 *     url = "https://httpbin.org/post",
 *     method = Method.POST,
 *     parameters = mapOf("name" to "Alex"),
 *     headers = mapOf("User-Agent" to "HttpRequest")
 * )
 * request.json<HttpBin> { result, response ->
 *     println(response.exception)
 *     println(response.success)
 *     println(result)
 * }
 *
 * ********* Config HttpURLConnection **************************************************************
 * val request = HttpRequest("https://httpbin.org/get", config = {
 *     it.readTimeout = 1000
 *     it.setRequestProperty("User-Agent", "HttpRequest")
 * })
 *
 * ********* Json **********************************************************************************
 * HttpRequest uses Kotlinx Serialization for Json.
 *
 * @Serializable data class HttpBin(
 *     val args: Map<String, String>? = null,
 *     val form: Map<String, String>? = null,
 *     val headers: Map<String, String>? = null
 * )
 *
 * val request = HttpRequest("https://httpbin.org/get")
 * request.json<HttpBin> { result, response ->
 *     println(result)
 * }
 * ********* Custom decoder ************************************************************************
 *
 * HttpRequest.json = Json {
 *     ignoreUnknownKeys = false
 * }
 *
 * ********* String body
 * HttpRequest("https://httpbin.org/get").response {
 *      println(Xml.parse(it.body))
 * }
 *
 * ********* Async *********************************************************************************
 * HttpRequest makes request and parsing asynchronously. Use runOnUiThread to change UI.
 *
 * request.json<Post> { post, response ->
 *     runOnUiThread {
 *         titleTextView.text = post.title
 *     }
 * }
 *
 *
 */
class HttpRequest(
    val path: String,
    val method: Method = Method.GET,
    val parameters: Map<String, Any> = mapOf(),
    val headers: Map<String, String> = HttpRequestConfig.DEFAULT_HEADERS,
    val config: ((HttpURLConnection) -> Unit)? = null,
    val postedData: String? = null
) {
    fun response(completion: (HttpResponse) -> Unit) {
        Executors.newSingleThreadExecutor().execute {
            try {
                val url = if ( parameters.isNotEmpty()) {
                    URL("${HttpRequestConfig.BASE_URL_MONGODB_SERVICE}${path}?${parameters.query}")
                } else {
                    URL("${HttpRequestConfig.BASE_URL_MONGODB_SERVICE}${path}")
                }

                val connection = url.openConnection() as HttpURLConnection

                connection.requestMethod = method.value

                headers.forEach { (key, value) ->
                    connection.setRequestProperty(key, value)
                }

                config?.let { it(connection) }

                if (method == Method.POST && !postedData.isNullOrEmpty()) {
//                    var gson = Gson()
//                    var jsonStr =  gson.toJson(postedData)

                    connection.doOutput = true
                    connection.outputStream.use {
                        it.write(postedData.toByteArray(Charsets.UTF_8))
                        it.flush()
                    }
                }

                val responseObj = HttpResponse()
                responseObj.connection = connection
                responseObj.body = connection.inputStream.use {
                    BufferedReader(InputStreamReader(it)).use { reader ->
                        reader.readText()
                    }
                }
                connection.disconnect()
                completion(responseObj)
            } catch (e: Exception) {
                val responseObj = HttpResponse()
                var errJson = JSONObject()
                errJson.put("message", e.message)

                responseObj.error = errJson
                completion(responseObj)
            }
        }
    }

    companion object {
        var json = Json { ignoreUnknownKeys = true }
    }

    inline fun json(crossinline completion: (Any?, HttpResponse) -> Unit) {
        response { response ->
            try {
                val responseJson = JSONObject(response.body)
                if( responseJson.get("status") == "success") {
//                    val result = json.decodeFromString<T>(responseJson.getJSONObject("data").toString())
                    completion(responseJson.getJSONObject("data"), response)
                }
                else {
                    response.error = responseJson.getJSONObject("data")
                    completion(null, response)
                }

            } catch (e: Exception) {
                completion(null, response)
            }
        }
    }

}

enum class Method(val value: String) {
    GET("GET"), HEAD("HEAD"), POST("POST"), PUT("PUT"),
    DELETE("DELETE"), OPTIONS("OPTIONS"), TRACE("TRACE"), PATCH("PATCH")
}

class HttpResponse {
    var connection: HttpURLConnection? = null
    var body: String? = null
    var error: JSONObject? = null
    val success: Boolean get() {
        connection?.let { return it.responseCode in 200..299 }
        return false
    }
}

val Map<String, Any>.query: String get() {
    return this.map { (key, value) -> "$key=${value.urlEncoded}" }.joinToString("&")
}

val Any.urlEncoded: String get() = URLEncoder.encode(toString(), "UTF-8")
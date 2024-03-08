package com.psi.onlineshop.httpRequest


import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.concurrent.Executors


/**
 *
 *         val request = HttpRequest(
 *             url = "http://172.30.1.27:3110/users",
 *             parameters = mapOf("email" to "cthutran@gmail.com")
 *         )
 *
 *         request.json<User> { result, response ->
 *             println("===========================================")
 *             println(response.body)
 *         }
 *
 * ******** POST *************************************
 * val request = HttpRequest(
 *     url = "http://172.30.1.27:3110/users",
 *     method = Method.POST,
 *     parameters = mapOf("name" to "Alex"),
 *     headers = mapOf("User-Agent" to "HttpRequest")
 * )
 * request.json<User> { result, response ->
 *     println(response.exception)
 *     println(response.success)
 *     println(result)
 * }
 *
 *
 * ******* Config HttpURLConnection ********************
 * val request = HttpRequest("https://httpbin.org/get", config = {
 *     it.readTimeout = 1000
 *     it.setRequestProperty("User-Agent", "HttpRequest")
 * })
 *
 *
 */
class HttpRequest(
    val url: String,
    val method: Method = Method.GET,
    val parameters: Map<String, Any> = mapOf(),
    val headers: Map<String, String> = mapOf(),
    val config: ((HttpURLConnection) -> Unit)? = null,
    val jsonDataStr: String = ""
) {
    fun response(completion: (HttpResponse) -> Unit) {
        Executors.newSingleThreadExecutor().execute {
            try {
                val url = if (method == Method.GET && parameters.isNotEmpty()) {
                    URL("$url?${parameters.query}")
                } else {
                    URL(url)
                }

//                val url = if ( parameters.isNotEmpty()) {
//                    URL("$url?${parameters.query}")
//                } else {
//                    URL(url)
//                }

                val connection = url.openConnection() as HttpURLConnection

                connection.requestMethod = method.value

                //con.setRequestProperty("Content-Type", "application/json");
                // con.setRequestProperty("Accept", "application/json");
                headers.forEach { (key, value) ->
                    connection.setRequestProperty(key, value)
                }

                config?.let { it(connection) }

                if (method == Method.POST && jsonDataStr.isNotEmpty()) {
//                    connection.doInput  =true
                    connection.doOutput = true
//
//                    connection.outputStream.use { os ->
//                        val input: ByteArray = jsonDataStr.toByteArray(Charsets.UTF_8)
//                        os.write(input, 0, input.size)
//                    }
////                    connection.outputStream.use {
////                        it.write(parameters.query.toByteArray())
////                    }
//
                    jsonDataStr?.let {
                        connection.outputStream.use {
                            it.write(jsonDataStr.toByteArray(Charsets.UTF_8))
                            it.flush()
                        println(jsonDataStr)
                        }
                    }

//                    val jsonObject = JSONObject()
//                    jsonObject.put("firstName", "last2")
//                    jsonObject.put("lastName", "last2")
//                    jsonObject.put("email", "test2@gmail.com")
//                    jsonObject.put("password", "123456")


//                    // Send the JSON we created
//                    val bw = BufferedWriter(OutputStreamWriter(connection.getOutputStream(), "UTF-8"))
//                    bw.write(jsonDataStr)
//                    bw.flush()
//                    bw.close()
//
////                    println(connection.)
//
////                    jsonDataStr?.let{
////                        val bw = BufferedWriter(OutputStreamWriter(connection.outputStream, "UTF-8"))
////                        bw.write(jsonDataStr)
////                        bw.flush()
////                        bw.close()
////                        println(jsonDataStr)
////                    }

//                    connection.doOutput = true
//                    connection.doInput = true
//                    // Send the JSON we created
//                    val outputStreamWriter = OutputStreamWriter(connection.outputStream)
//                    outputStreamWriter.write(jsonDataStr)
//                    outputStreamWriter.flush()

                }

//                connection.doInput = true
//                connection.inputStream.bufferedReader().use {
//                    it.readText()
//                }

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
                responseObj.exception = e
                completion(responseObj)
            }
        }
    }

    companion object {
        var json = Json { ignoreUnknownKeys = true }
    }

    inline fun <reified T> json(crossinline completion: (T?, HttpResponse) -> Unit) where T : Any {
        response { response ->
            try {
                val body = response.body
//                println(body)
                val result = if (body != null) json.decodeFromString<T>(body) else null
                completion(result, response)
            } catch (e: Exception) {
                response.exception = e
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
    var exception: Exception? = null
    val success: Boolean get() {
        connection?.let { return it.responseCode in 200..299 }
        return false
    }
}

val Map<String, Any>.query: String get() {
    return this.map { (key, value) -> "$key=${value.urlEncoded}" }.joinToString("&")
}

val Any.urlEncoded: String get() = URLEncoder.encode(toString(), "UTF-8")
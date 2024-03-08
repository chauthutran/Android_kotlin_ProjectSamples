package com.psi.onlineshop.httpRequest

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

import android.content.Context
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset
import android.app.PendingIntent.getActivity

/**
 * Created by Rohan Jahagirdar on 07-02-2018.
 */


import java.util.HashMap

//import okhttp3.Call
//import okhttp3.Callback
//import okhttp3.FormBody
//import okhttp3.MediaType
//import okhttp3.OkHttpClient
//import okhttp3.Request
//import okhttp3.RequestBody
import java.util.Map
//
//class OkHttpRequest(client: OkHttpClient) {
//    internal var client = OkHttpClient()
//
//    init {
//        this.client = client
//    }
//
//    fun POST(url: String, parameters: HashMap<String, String>, callback: Callback): Call {
//        val builder = FormBody.Builder()
//        val it = parameters.entries.iterator()
//        while (it.hasNext()) {
//            val pair = it.next() as Map.Entry<*, *>
//            builder.add(pair.key.toString(), pair.value.toString())
//        }
//
//        val formBody = builder.build()
//        val request = Request.Builder()
//            .url(url)
//            .post(formBody)
//            .build()
//
//
//        val call = client.newCall(request)
//        call.enqueue(callback)
//        return call
//    }
//
//    fun GET(url: String, callback: Callback): Call {
//        val request = Request.Builder()
//            .url(url)
//            .build()
//
//        val call = client.newCall(request)
//        call.enqueue(callback)
//        return call
//    }
//
//    companion object {
//        val JSON = MediaType.parse("application/json; charset=utf-8")
//    }
//}

fun sendGetRequest(urlString: String): StringBuffer {

//    var reqParam = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(userName, "UTF-8")
//    reqParam += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8")

    val mURL = URL(urlString)

    with(mURL.openConnection() as HttpURLConnection) {
        // optional default is GET
        requestMethod = "GET"

        println("---- URL : $url")
        println("---- Response Code : $responseCode")

        BufferedReader(InputStreamReader(inputStream)).use {
            val response = StringBuffer()

            var inputLine = it.readLine()
            while (inputLine != null) {
                response.append(inputLine)
                inputLine = it.readLine()
            }
            it.close()

            return response
        }
    }
}

fun sendPostRequest(urlString: String): StringBuffer {

//    var reqParam = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(userName, "UTF-8")
//    reqParam += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8")
    val mURL = URL(urlString)

    with(mURL.openConnection() as HttpURLConnection) {
        // optional default is GET
        requestMethod = "POST"

        val wr = OutputStreamWriter(getOutputStream());
//        wr.write(reqParam);
        wr.flush();

        println("URL : $url")
        println("Response Code : $responseCode")

        BufferedReader(InputStreamReader(inputStream)).use {
            val response = StringBuffer()

            var inputLine = it.readLine()
            while (inputLine != null) {
                response.append(inputLine)
                inputLine = it.readLine()
            }
            it.close()

            return response
        }
    }
}

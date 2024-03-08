package com.psi.onlineshop.fragments.loginregister

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.psi.onlineshop.R
import com.psi.onlineshop.data.User
import com.psi.onlineshop.databinding.FragmentRegisterBinding
import com.psi.onlineshop.httpRequest.FetchCompleteListener
import com.psi.onlineshop.httpRequest.HttpRequest
import com.psi.onlineshop.httpRequest.Method
//import com.psi.onlineshop.httpRequest.OkHttpRequest
import com.psi.onlineshop.httpRequest.sendGetRequest
import com.psi.onlineshop.utils.Resource
import com.psi.onlineshop.utils.UserRegisterValidation
//import com.psi.onlineshop.utils.sendGetRequest
import com.psi.onlineshop.viewmodels.RegisterViewModel
//import io.github.rybalkinsd.kohttp.dsl.httpGet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
//import okhttp3.OkHttpClient
//import okhttp3.Request
//import org.bson.types.ObjectId
//import org.bson.types.ObjectId

//import me.rohanjahagirdar.outofeden.Utils.FetchCompleteListener
//import okhttp3.Call
//import okhttp3.Callback
//import okhttp3.OkHttpClient
//import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//
//        val request = HttpRequest(
//            url = "http://172.30.1.27:3110/users",
//            parameters = mapOf("email" to "cthutran@gmail.com")
//        )
//
//
//        request.json<User> { result, response ->
//            println("===========================================")
//            println(response.body)
//        }


        var user = User("test3", "last3", "test3@gmail.com", "123456", true )
        var gson = Gson()
        var jsonStr =  gson.toJson(user)

        val request = HttpRequest(
            url = "http://172.30.1.27:3110/users",
            method = Method.POST,
//            parameters = mapOf(
//                "firstName" to "test2",
//                "lastName" to "last2",
//                "email" to "test2@gmail.com",
//                "password" to "123456",
//                ),
            jsonDataStr = jsonStr,

            //con.setRequestProperty("Content-Type", "application/json");
            // con.setRequestProperty("Accept", "application/json");

            headers = mapOf("User-Agent" to "HttpRequest","Content-Type" to "application/json", "Accept" to "application/json")
        )
        request.json<User> { result, response ->
            println("===========================================")
            println(response.exception)
            println(response.success)
            println(result)
        }

//        val response: Response = httpGet {
//            host = "google.com"
//            path = "/search"
//            param {
//                "q" to "iphone"
//                "safe" to "off"
//            }
//        }

//        sendGetRequest("http://172.30.1.27:3110")


//        val client = OkHttpClient()
//        val url = URL("http://172.30.1.27:3110")
//
//        val request = Request.Builder()
//            .url(url)
//            .get()
//            .build()
//
//        val response = client.newCall(request).execute()
//
//        val responseBody = response.body!!.string()
//
//        //Response
//        println("Response Body: " + responseBody)



//        //we could use jackson if we got a JSON
//        val mapperAll = ObjectMapper()
//        val objData = mapperAll.readTree(responseBody)
//
//        objData.get("data").forEachIndexed { index, jsonNode ->
//            println("$index $jsonNode")
//        }


//        runBlocking {

//            var client = OkHttpClient()
//            var request= OkHttpRequest(client)
//
//            var url = "http://172.30.1.27:3110"
//            request.GET(url, object: Callback {
//                override fun onResponse(call: Call?, response: Response) {
//                    val responseData = response.body()?.string()
//                    runOnUiThread{
//                        try {
//                            println("--------- docs: ${responseData}")
////                            var json = JSONObject(responseData)
////                            println("Request Successful!!")
////                            println(json)
////                            val responseObject = json.getJSONObject("response")
////                            val docs = json.getJSONArray("docs")
////                            println("--------- docs: ${docs}")
////                            this@MainActivity.fetchComplete()
//                        } catch (e: JSONException) {
//                            e.printStackTrace()
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call?, e: IOException?) {
//                    println("Request Failure.")
//                }
//            })
//        }

        binding.apply {
            buttonRegisterRegister.setOnClickListener{
//                val user = User(
//                    ObjectId(),
//                    editFirstNameRegister.text.toString().trim(),
//                    editLastNameRegister.text.toString().trim(),
//                    editEmailRegister.text.toString().trim(),
//                    editPasswordRegister.text.toString()
//                )
//
//
//                println("=============== user : ${user}")
//                viewModel.createUserAcccount(user)
            }

            tvLoginQuestion.setOnClickListener{
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.register.collect{
                when(it){
                    is Resource.Loading -> {
                        binding.buttonRegisterRegister.startAnimation()
                    }
                    is Resource.Success -> {
                        Toast.makeText(requireContext(), "Register successfully", Toast.LENGTH_SHORT).show()
                        binding.buttonRegisterRegister.revertAnimation()
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                        binding.buttonRegisterRegister.revertAnimation()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect {
                if( it.email is UserRegisterValidation.Failed ) {
                    withContext(Dispatchers.Main) {
                        binding.editEmailRegister.apply {
                            requestFocus()
                            error = it.email.message
                        }
                    }
                }

                if( it.password is UserRegisterValidation.Failed ) {
                    withContext(Dispatchers.Main) {
                        binding.editPasswordRegister.apply {
                            requestFocus()
                            error = it.password.message
                        }
                    }
                }

            }
        }

//        rawJSON()
    }

    fun rawJSON() {

//        var user = User("test2", "last2", "test2@gmail.com", "123456")
//        var gson = Gson()
//        var jsonStr =  gson.toJson(user)

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("firstName", "last2")
        jsonObject.put("lastName", "last2")
        jsonObject.put("email", "test2@gmail.com")
        jsonObject.put("password", "123456")

        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

//       runBlocking {
            val url = URL("http://172.30.1.27:3110/users")
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.requestMethod = "POST"
            httpURLConnection.setRequestProperty("Content-Type", "application/json") // The format of the content we're sending to the server
            httpURLConnection.setRequestProperty("Accept", "application/json") // The format of response we want to get from the server
            httpURLConnection.doInput = true
            httpURLConnection.doOutput = true

        println("---------- 1")
            // Send the JSON we created
            val outputStreamWriter = OutputStreamWriter(httpURLConnection.outputStream)
        println("---------- 2")
            outputStreamWriter.write(jsonObjectString)
        println("---------- 3")
            outputStreamWriter.flush()
        println("---------- 12")

            // Check if the connection is successful
            val responseCode = httpURLConnection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = httpURLConnection.inputStream.bufferedReader()
                    .use { it.readText() }  // defaults to UTF-8
//                withContext(Dispatchers.Main) {
println(" ------------- response: ${response}")
//                    // Convert raw JSON to pretty JSON using GSON library
//                    val gson = GsonBuilder().setPrettyPrinting().create()
//                    val prettyJson = gson.toJson(JsonParser.parseString(response))
//                    println("Pretty Printed JSON : ${prettyJson}")

//                }
                println("---------- 3")
            } else {
               println(" ==================== ${responseCode}")
            }
        println("---------- 4")
//        }
    }

//    override fun fetchComplete() {
//
//    }
}
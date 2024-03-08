package com.psi.onlineshop.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.psi.onlineshop.data.User
import com.psi.onlineshop.httpRequest.HttpRequest
import com.psi.onlineshop.httpRequest.HttpRequestConfig
import com.psi.onlineshop.httpRequest.HttpRequestUtil
import com.psi.onlineshop.httpRequest.Method
import com.psi.onlineshop.utils.Resource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.json.JSONObject


class LoginViewModel (
    application: Application
): AndroidViewModel(application) {

    private val _login = MutableSharedFlow<Resource<User>>()
    val login = _login.asSharedFlow()

    private val _resetPassword = MutableSharedFlow<Resource<String>>()
    val resetPassword = _resetPassword.asSharedFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch { _login.emit(Resource.Loading()) }

        viewModelScope.launch {

            var searchConditions = JSONObject()
            searchConditions.put("email", email)
            searchConditions.put("password", password)

            var searchData = JSONObject()
            searchData.put("operator", HttpRequestConfig.SEARCH_OPERATOR_AND)
            searchData.put("condition", searchConditions)

            val request = HttpRequest(
                path = "/users",
                method = Method.POST,
                parameters = mapOf("action" to HttpRequestConfig.REQUEST_ACTION_FIND),
                postedData = searchData.toString()
//                parameters = mapOf( "email" to email, "password" to password )
            )
            request.json{ result, response ->
                println("====================== ")
                println(response.error)
                    println(response.success)
                    println(result)

                if( response.error != null )
                {
                    val message = response.error?.getString("message") ?: ""
                    viewModelScope.launch { _login.emit(Resource.Error(message)) }
                }
                else if(result != null )
                {
                    val users = HttpRequestUtil.convertJsonToObj<List<User>>(result)
                    if( users.isEmpty() ) {
                        viewModelScope.launch { _login.emit(Resource.Error("The email or password is wrong")) }
                    }
                    else {
                        viewModelScope.launch { _login.emit(Resource.Success(users.get(0))) }
                    }
                }
            }
        }

//
//        var query = Filters.and(
//            listOf(
//                Filters.eq("email", email),
//                Filters.eq("password", password)
//            )
//        )
//
//        viewModelScope.launch {
//           var result = database.getCollection<User>(Constants.USER_COLLECTION).find<User>(filter = query)
//            result.collect {
//                println(it)
//            }
//        }

    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            _resetPassword.emit(Resource.Loading())

//            firebaseAuth.sendPasswordResetEmail(email)
//                .addOnSuccessListener {
//                    viewModelScope.launch {
//                        _resetPassword.emit(Resource.Success(email))
//                    }
//                }
//                .addOnFailureListener {
//                    viewModelScope.launch {
//                        _resetPassword.emit(Resource.Error(it.message.toString()))
//                    }
//                }
        }
    }
}
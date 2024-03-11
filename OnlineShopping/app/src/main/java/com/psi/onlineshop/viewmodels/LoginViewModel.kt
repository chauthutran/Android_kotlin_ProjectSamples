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
            searchData.put("payload", searchConditions)
            searchData.put("collectionName", HttpRequestConfig.COLLECTION_USERS)

            val request = HttpRequest(
                method = Method.POST,
                parameters = mapOf("action" to HttpRequestConfig.REQUEST_ACTION_FIND),
                postedData = searchData.toString()
            )
            request.json<User> { result, response ->
                if( response.error != null )
                {
                    val message = response.error?.getString("message") ?: ""
                    viewModelScope.launch { _login.emit(Resource.Error(message)) }
                }
                else if(result != null && result is List<*> )
                {
                    if( result.isEmpty()) {
                        viewModelScope.launch { _login.emit(Resource.Error("The email or password is wrong")) }
                    }
                    else {
                        viewModelScope.launch { _login.emit(Resource.Success(result.get(0) as User)) }
                    }
                }
            }
        }

    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            _resetPassword.emit(Resource.Loading())
        }
    }
}
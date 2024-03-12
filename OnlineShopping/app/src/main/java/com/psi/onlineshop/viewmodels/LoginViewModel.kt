package com.psi.onlineshop.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.psi.onlineshop.data.User
import com.psi.onlineshop.httpRequest.HttpRequestConfig
import com.psi.onlineshop.httpRequest.HttpRequestUtil
import com.psi.onlineshop.utils.Resource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.json.JSONObject


class LoginViewModel (
    application: Application
): AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

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

            HttpRequestUtil.sendPOSTRequest(context, HttpRequestConfig.REQUEST_ACTION_FIND, HttpRequestConfig.COLLECTION_USERS, searchConditions) { response ->
               if( response is JSONObject)
               {
                   var status = response.getString("status")
                   var data = response.getJSONArray("data")
                   if( status == HttpRequestConfig.RESPONSE_STATUS_SUCCESS && data.length() > 0 ) {
                       val user = HttpRequestUtil.convertJsonToObj<User>(data.getJSONObject(0))
                       viewModelScope.launch { _login.emit(Resource.Success(user)) }
                   }
                   else {
                       viewModelScope.launch { _login.emit(Resource.Error("The email or password is wrong")) }
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
package com.psi.onlineshop.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.psi.onlineshop.data.User
import com.psi.onlineshop.httpRequest.HttpRequestConfig
import com.psi.onlineshop.httpRequest.HttpRequestUtil
import com.psi.onlineshop.utils.Resource
import com.psi.onlineshop.utils.UserRegisterFieldsState
import com.psi.onlineshop.utils.UserRegisterValidation
import com.psi.onlineshop.utils.validateEmail
import com.psi.onlineshop.utils.validatePassword
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

class RegisterViewModel(
    application: Application
): AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext


    private var _register = MutableStateFlow<Resource<User>>(Resource.UnSpecified())
    var register: Flow<Resource<User>> = _register

    private val _validation = Channel<UserRegisterFieldsState>()
    val validation = _validation.receiveAsFlow()


    fun createUserAcccount(user: User) {
        if( checkValidation(user) ) {

            viewModelScope.launch { _register.emit(Resource.Loading()) }

            viewModelScope.launch {

                var payload = HttpRequestUtil.convertObjToJson(user)

                HttpRequestUtil.sendPOSTRequest(context, HttpRequestConfig.REQUEST_ACTION_ADD_ONE, HttpRequestConfig.COLLECTION_USERS, payload) { response ->
                    if( response is JSONObject)
                    {
                        var status = response.getString("status")
                        var data = response.getJSONArray("data")
                        if( status == HttpRequestConfig.RESPONSE_STATUS_SUCCESS && data.length() > 0 ) {
                            val user = HttpRequestUtil.convertJsonToObj<User>(data.getJSONObject(0))
                            viewModelScope.launch { _register.emit(Resource.Success(user)) }
                        }
                        else {
                            val message = response.getString("data")
                            viewModelScope.launch { _register.emit(Resource.Error(message)) }
                        }
                    }
                }
            }

        }
        else
        {
            val registerFieldsState = UserRegisterFieldsState( validateEmail(user.email), validatePassword(user.password))
            runBlocking {
                _validation.send(registerFieldsState)
            }
        }
    }

    private fun checkValidation(user: User): Boolean {
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(user.password)

        return emailValidation is UserRegisterValidation.Success && passwordValidation is UserRegisterValidation.Success
    }

}
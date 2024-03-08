package com.psi.onlineshop.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
//import com.mongodb.kotlin.client.coroutine.MongoDatabase
//import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.psi.onlineshop.ShoppingApplication
import com.psi.onlineshop.data.User
import com.psi.onlineshop.httpRequest.HttpRequest
import com.psi.onlineshop.httpRequest.Method
import com.psi.onlineshop.utils.Constants
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
//import org.bson.Document

class RegisterViewModel(
    application: Application
): AndroidViewModel(application) {

    private var _register = MutableStateFlow<Resource<User>>(Resource.UnSpecified())
    var register: Flow<Resource<User>> = _register

    private val _validation = Channel<UserRegisterFieldsState>()
    val validation = _validation.receiveAsFlow()

//    var database: MongoDatabase = ShoppingApplication.getDatabase(application.applicationContext)


    fun createUserAcccount(user: User) {
        if( checkValidation(user) ) {

            viewModelScope.launch { _register.emit(Resource.Loading()) }

            viewModelScope.launch {

                val request = HttpRequest(
                    path = "/users",
                    method = Method.POST,
                    postedData = user
                )
                request.json<User> { result, response ->
                    if( response.error != null )
                    {
                        val message = response.error?.getString("message") ?: ""
                        viewModelScope.launch { _register.emit(Resource.Error(message)) }
                    }
                    else if(result != null )
                    {
                        viewModelScope.launch { _register.emit(Resource.Success(result)) }
                    }
//                    println(response.error)
//                    println(response.success)
//                    println(result)
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
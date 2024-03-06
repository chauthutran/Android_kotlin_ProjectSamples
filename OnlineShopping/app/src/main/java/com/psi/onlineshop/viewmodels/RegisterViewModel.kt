package com.psi.onlineshop.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
//import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.psi.onlineshop.ShoppingApplication
import com.psi.onlineshop.data.User
import com.psi.onlineshop.utils.Constants
import com.psi.onlineshop.utils.Resource
import com.psi.onlineshop.utils.UserRegisterFieldsState
import com.psi.onlineshop.utils.UserRegisterValidation
import com.psi.onlineshop.utils.validateEmail
import com.psi.onlineshop.utils.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
//import org.bson.Document
import javax.inject.Inject

class RegisterViewModel(
    application: Application
): AndroidViewModel(application) {

    private var _register = MutableStateFlow<Resource<User>>(Resource.UnSpecified())
    var register: Flow<Resource<User>> = _register

    private val _validation = Channel<UserRegisterFieldsState>()
    val validation = _validation.receiveAsFlow()

//    private var database: MongoDatabase = ShoppingApplication.getDatabase(application.applicationContext)

    fun createAcccountWithEmailAndPassword(user: User, password: String) {
        if( checkValidation(user, password) ) {

            viewModelScope.launch {
                _register.emit(Resource.Loading())
            }

//            viewModelScope.launch {
//                var collection = database.getCollection<User>(Constants.USER_COLLECTION)
//                collection.insertOne(user).also {
//                    println("Add new user with id : ${it.insertedId}")
//                }
//            }

        }
        else
        {
            val registerFieldsState = UserRegisterFieldsState( validateEmail(user.email), validatePassword(password))
            runBlocking {
                _validation.send(registerFieldsState)
            }
        }
    }

    private fun checkValidation(user: User, password: String): Boolean {
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)
        val canRegister = emailValidation is UserRegisterValidation.Success && passwordValidation is UserRegisterValidation.Success

        return canRegister
    }

}
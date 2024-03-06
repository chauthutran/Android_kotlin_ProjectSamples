package com.psi.onlineshop.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
//import com.mongodb.client.model.Filters
//import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.psi.onlineshop.ShoppingApplication
import com.psi.onlineshop.data.User
import com.psi.onlineshop.utils.Constants
import com.psi.onlineshop.utils.Resource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch


class LoginViewModel (
    application: Application
): AndroidViewModel(application) {

    private val _login = MutableSharedFlow<Resource<User>>()
    val login = _login.asSharedFlow()

    private val _resetPassword = MutableSharedFlow<Resource<String>>()
    val resetPassword = _resetPassword.asSharedFlow()

//    private var database: MongoDatabase = ShoppingApplication.getDatabase(application.applicationContext)


    fun login(email: String, password: String) {
        viewModelScope.launch { _login.emit(Resource.Loading()) }
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
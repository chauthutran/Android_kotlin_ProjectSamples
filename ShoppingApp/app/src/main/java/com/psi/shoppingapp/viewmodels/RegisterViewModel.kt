package com.psi.shoppingapp.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.psi.shoppingapp.data.User
import com.psi.shoppingapp.utils.Constants
import com.psi.shoppingapp.utils.RegisterFieldsState
import com.psi.shoppingapp.utils.RegisterValidation
import com.psi.shoppingapp.utils.Resource
import com.psi.shoppingapp.utils.validateEmail
import com.psi.shoppingapp.utils.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
): ViewModel() {

    private var _register = MutableStateFlow<Resource<User>>(Resource.UnSpecified())
    var register: Flow<Resource<User>> = _register

    private val _validation = Channel<RegisterFieldsState>()
    val validation = _validation.receiveAsFlow()

    fun createAcccountWithEmailAndPassword(user: User, password: String) {
        if( checkValidation(user, password) ) {

            runBlocking {
                _register.emit(Resource.Loading())
            }

            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener {
                    it.user?.let {
                        saveUserInfo(it.uid, user)
                    }
                }.addOnFailureListener {
                    _register.value = Resource.Error(it.message.toString())
                }
        }
        else
        {
            val registerFieldsState = RegisterFieldsState( validateEmail(user.email), validatePassword(password))
            runBlocking {
                _validation.send(registerFieldsState)
            }
        }
    }

    private fun checkValidation(user: User, password: String): Boolean {
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)
        val canRegister = emailValidation is RegisterValidation.Success && passwordValidation is RegisterValidation.Success

        return canRegister
    }

    private fun saveUserInfo(userUid: String, user: User){
        db.collection(Constants.USER_COLLECTION)
            .document(userUid)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)
            }
            .addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())
            }
    }
}
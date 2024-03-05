package com.psi.shoppingapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.psi.shoppingapp.data.CartProduct
import com.psi.shoppingapp.data.User
import com.psi.shoppingapp.utils.Constants
import com.psi.shoppingapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
): ViewModel(){

    private val _user = MutableStateFlow<Resource<User>>(Resource.UnSpecified() )
    val user = _user.asStateFlow()

    init {
        getUser()
    }

    fun getUser() {
        viewModelScope.launch { _user.emit(Resource.Loading() ) }

        firestore.collection(Constants.USER_COLLECTION).document(auth.uid!!)
            .addSnapshotListener { value, error ->
                if(error != null || value == null ) {
                    viewModelScope.launch { _user.emit(Resource.Error(error?.message.toString())) }
                }
                else {
                    var user = value?.toObject(User::class.java)
                    user?.let {
                        viewModelScope.launch { _user.emit(Resource.Success(user) ) }
                    }
                }
            }
    }

    fun logout() {
        auth.signOut()
    }
}
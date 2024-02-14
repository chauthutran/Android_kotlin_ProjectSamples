package com.psi.shoppingapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.psi.shoppingapp.data.Address
import com.psi.shoppingapp.utils.Constants
import com.psi.shoppingapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel()  {

    private val _addressList = MutableStateFlow<Resource<List<Address>>>(Resource.UnSpecified())
    val addressList = _addressList.asStateFlow()

    init{
        getUserAddress()
    }

    fun getUserAddress() {
        viewModelScope.launch { _addressList.emit(Resource.Loading()) }

        firestore.collection( Constants.USER_COLLECTION ).document(auth.uid!!).collection( Constants.ADDRESS_COLLECTION )
            .addSnapshotListener{ value, error ->
                if( error != null )
                    viewModelScope.launch { _addressList.emit(Resource.Error(error.message.toString())) }
                else {
                    var addressList = value?.toObjects(Address::class.java)
                    viewModelScope.launch { _addressList.emit(Resource.Success(addressList!!)) }
                }
            }
    }
}
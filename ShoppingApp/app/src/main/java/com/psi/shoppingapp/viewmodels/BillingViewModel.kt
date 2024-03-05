package com.psi.shoppingapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
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

    private fun getUserAddress() {
        viewModelScope.launch { _addressList.emit(Resource.Loading()) }

        firestore.collection( Constants.USER_COLLECTION ).document(auth.uid!!).collection( Constants.USER_ADDRESS_COLLECTION )
            .addSnapshotListener{ value, error ->
                if( error != null )
                    viewModelScope.launch { _addressList.emit(Resource.Error(error.message.toString())) }
                else {
//                    var addressList = convertToAddressObject(value)
                    var addressList = value?.toObjects(Address::class.java)
                    viewModelScope.launch { _addressList.emit(Resource.Success(addressList!!)) }
                }
            }
    }

//    private fun convertToAddressObject(queryResult: QuerySnapshot?): ArrayList<Address> {
//        var addressList = ArrayList<Address>()
//
//        queryResult?.forEach {
//            var address = it.toObject(Address::class.java)
//            address.id = it.id
//            addressList.add( address )
//        }
//
//        return addressList
//    }
}
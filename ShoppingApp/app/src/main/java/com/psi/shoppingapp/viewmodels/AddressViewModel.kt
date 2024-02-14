package com.psi.shoppingapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.psi.shoppingapp.data.Address
import com.psi.shoppingapp.utils.Constants
import com.psi.shoppingapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
): ViewModel() {

    private val _addNewAddressStatus = MutableStateFlow<Resource<Address>>(Resource.UnSpecified())
    val addNewAddressStatus = _addNewAddressStatus.asStateFlow()

    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()


    fun addAddress(address: Address) {
        val validate = validateInputs( address )

        if( validate ) {
            viewModelScope.launch { _addNewAddressStatus.emit( Resource.Loading() ) }
            firestore.collection(Constants.USER_COLLECTION).document(auth.uid!!)
                .collection(Constants.ADDRESS_COLLECTION).document()
                .set(address)
                .addOnSuccessListener {
                    viewModelScope.launch { _addNewAddressStatus.emit( Resource.Success(address)) }
                }
                .addOnFailureListener {
                    viewModelScope.launch { _addNewAddressStatus.emit( Resource.Error(it.message.toString())) }
                }
        }
        else
        {
            viewModelScope.launch { _error.emit( "All fields are required" ) }
        }
    }

    private fun validateInputs( address: Address ): Boolean {
        return address.addressTitle.trim().isNotEmpty()
                && address.fullName.trim().isNotEmpty()
                && address.phone.trim().isNotEmpty()
                && address.street.trim().isNotEmpty()
                && address.state.trim().isNotEmpty()
                && address.city.trim().isNotEmpty()
    }
}
package com.psi.shoppingapp.viewmodels

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.psi.shoppingapp.data.order.Order
import com.psi.shoppingapp.utils.Constants
import com.psi.shoppingapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
): ViewModel() {

    private val _order = MutableStateFlow<Resource<Order>>( Resource.UnSpecified() )
    val order = _order.asStateFlow()

    fun placeOrder( order: Order ) {
        viewModelScope.launch { _order.emit( Resource.Loading() ) }

        // "runBatch" is used to run many tasks the same time. If one of the tasks is failed, all tasks will be failed
        firestore.runBatch{ batch ->

            // Add the order into the user-orders collection
            firestore.collection(Constants.USER_COLLECTION).document(auth.uid!!)
                .collection( Constants.USER_ORDERS_COLLECTION).document()
                .set( order )

            // Add the order into the orders collections
            firestore.collection( Constants.ORDERS_COLLECTION ).document()
                .set( order )

            // Delete products from user-cart collection
            firestore.collection(Constants.USER_COLLECTION).document(auth.uid!!)
                .collection( Constants.USER_CART_COLLECTION).get()
                .addOnSuccessListener {
                    it.documents.forEach {
                        it.reference.delete()
                    }
                }

        }.addOnSuccessListener {
            viewModelScope.launch { _order.emit(Resource.Success(order)) }
        }
        .addOnFailureListener {
            viewModelScope.launch { _order.emit(Resource.Error(it.message.toString())) }
        }
    }
}
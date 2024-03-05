package com.psi.shoppingapp.viewmodels

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
class OrderListViewModel @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val auth: FirebaseAuth
): ViewModel() {

    private val _allOrders = MutableStateFlow<Resource<List<Order>>>(Resource.UnSpecified())
    val allOrders = _allOrders.asStateFlow()

    init {
        getOrderList()
    }

    private fun getOrderList() {
        viewModelScope.launch { _allOrders.emit(Resource.Loading()) }

        fireStore.collection(Constants.USER_COLLECTION).document(auth.uid!!).collection(Constants.USER_ORDERS_COLLECTION).get()
            .addOnSuccessListener {
                var orders = it.toObjects(Order::class.java)
                viewModelScope.launch { _allOrders.emit( Resource.Success(orders)) }
            }.addOnFailureListener {
                viewModelScope.launch { _allOrders.emit(Resource.Error(it.message.toString())) }
            }
    }
}
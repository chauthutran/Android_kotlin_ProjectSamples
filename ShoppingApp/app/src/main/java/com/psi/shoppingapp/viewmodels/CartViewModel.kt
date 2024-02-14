package com.psi.shoppingapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.psi.shoppingapp.data.CartProduct
import com.psi.shoppingapp.fhirebase.FirebaseCommon
import com.psi.shoppingapp.helper.getProductPrice
import com.psi.shoppingapp.utils.Constants
import com.psi.shoppingapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
): ViewModel() {

    private val _cartProducts = MutableStateFlow<Resource<List<CartProduct>>>(Resource.UnSpecified())
    val cartProducts = _cartProducts.asStateFlow()

    private var cartProductDocuments = emptyList<DocumentSnapshot>()

    val productsPrice = cartProducts.map {
        when (it) {
            is Resource.Success -> {
                calculatePrice(it.data!!)
            }
            else -> null
        }
    }

    private val _deleteDialog = MutableSharedFlow<CartProduct>()
    val deleteDialog = _deleteDialog.asSharedFlow()

    fun deleteCartProduct( cartProduct: CartProduct ) {
        val index = cartProducts.value.data?.indexOf(cartProduct)
        if( index != null && index != -1 )
        {
            val documentId = cartProductDocuments[index].id
            firestore.collection( Constants.USER_COLLECTION ).document(auth.uid!!).collection(Constants.CART_COLLECTION)
                .document(documentId).delete()
        }
    }

    private fun calculatePrice(data: List<CartProduct>): Float {
        return data.sumByDouble { cartProduct ->
            ( cartProduct.product.offerPercentage.getProductPrice( cartProduct.product.price ) * cartProduct.quantity ).toDouble()
        }.toFloat()
    }


    init {
        getCartProducts()
    }

    private fun getCartProducts() {
        viewModelScope.launch {
            _cartProducts.emit(Resource.Loading())
        }

        // When a new product added, this function is called
        firestore.collection( Constants.USER_COLLECTION).document(auth.uid!!).collection(Constants.CART_COLLECTION)
            .addSnapshotListener { value, error ->
                if(error != null || value == null ) {
                    viewModelScope.launch { _cartProducts.emit(Resource.Error(error?.message.toString())) }
                }
                else {
                    cartProductDocuments = value.documents
                    var cartProducts = value.toObjects(CartProduct::class.java)
                    viewModelScope.launch { _cartProducts.emit( Resource.Success(cartProducts) ) }
                }
            }
    }

    fun changeQuantity( cartProduct: CartProduct, quantityChanging: FirebaseCommon.QuantityChanging ) {
        val index = cartProducts.value.data?.indexOf(cartProduct)

        /**
         * Index should be equals to -1 if the function [getCartProducts] delays which will also delay the result we expert
         * to be inside the [_cartProducts] and to prevent the app from crashing we make a check
         */
        if( index != null && index != -1 ) {
            val documentId = cartProductDocuments[index].id
            when( quantityChanging ) {
                FirebaseCommon.QuantityChanging.INCREASE -> {
                    viewModelScope.launch { _cartProducts.emit(Resource.Loading()) }
                    increaseQuantity( documentId )
                }

                FirebaseCommon.QuantityChanging.DECREASE -> {

                    if( cartProduct.quantity == 1 )
                    {
                        viewModelScope.launch { _deleteDialog.emit(cartProduct) }
                        return
                    }
                    viewModelScope.launch { _cartProducts.emit(Resource.Loading()) }
                    decreaseQuantity( documentId )
                }
                
                else -> Unit
            }

        }
    }

    private fun increaseQuantity(documentId: String) {
        firebaseCommon.increaseQuantity(documentId) { result, exception ->
            if( exception != null ) {
                viewModelScope.launch { _cartProducts.emit( Resource.Error(exception.message.toString()) ) }
            }
        }
    }

    private fun decreaseQuantity(documentId: String) {
        firebaseCommon.decreaseQuantity(documentId) { result, exception ->
            if( exception != null ) {
                viewModelScope.launch { _cartProducts.emit( Resource.Error(exception.message.toString()) ) }
            }
        }
    }
}
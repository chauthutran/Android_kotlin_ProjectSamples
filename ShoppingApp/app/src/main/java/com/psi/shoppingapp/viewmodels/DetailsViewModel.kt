package com.psi.shoppingapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.psi.shoppingapp.data.CartProduct
import com.psi.shoppingapp.fhirebase.FirebaseCommon
import com.psi.shoppingapp.utils.Constants
import com.psi.shoppingapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    val auth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
): ViewModel() {

    private val _addToCart = MutableStateFlow<Resource<CartProduct>>(Resource.UnSpecified())
    val addToCart = _addToCart.asStateFlow()

    fun addUpdateProductInCart( cartProduct: CartProduct ) {
        viewModelScope.launch {  _addToCart.emit(Resource.Loading()) }

        firestore.collection( Constants.USER_COLLECTION ).document( auth.uid!! )
            .collection( Constants.CART_COLLECTION )
            .whereEqualTo("product.id", cartProduct.product.id )
            .get()
            .addOnSuccessListener {
                it.documents.let {
                    if( it.isEmpty() ) { // No any product in the cart
                        addNewProduct( cartProduct )
                    }
                    else {
                        val product = it.first().toObject(CartProduct::class.java)
                        if( product == cartProduct ) { // Increase quantity
                            var documentId = it.first().id
                            increaseQuantity( documentId, cartProduct)
                        }
                        else { // Add new product
                            addNewProduct( cartProduct )
                        }
                    }
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {  _addToCart.emit(Resource.Error(it.message.toString())) }
            }
    }

    private fun addNewProduct( cartProduct: CartProduct ) {
        firebaseCommon.addProductToCart(cartProduct) { addedProduct, e ->
            viewModelScope.launch {
                if( e == null )
                    _addToCart.emit( Resource.Success(addedProduct!!))
                else
                    _addToCart.emit( Resource.Error(e.message.toString()))
            }
        }
    }

    private fun increaseQuantity( documentId: String, cartProduct: CartProduct ) {
        firebaseCommon.increaseQuantity( documentId ) { addedId, e ->
            viewModelScope.launch {
                if (e == null)
                    _addToCart.emit(Resource.Success(cartProduct!!))
                else
                    _addToCart.emit(Resource.Error(e.message.toString()))
            }
        }
    }

}
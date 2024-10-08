package com.psi.shoppingapp.fhirebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.psi.shoppingapp.data.CartProduct
import com.psi.shoppingapp.utils.Constants

class FirebaseCommon (
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private val cartCollection = firestore.collection( Constants.USER_COLLECTION ).document(auth.uid!!).collection( Constants.USER_CART_COLLECTION )

    fun addProductToCart( cartProduct: CartProduct, onResult: (CartProduct?, Exception?) -> Unit ) {
        cartCollection.document().set(cartProduct)
            .addOnSuccessListener {
                onResult( cartProduct, null )
            }
            .addOnFailureListener {
                onResult( null, it )
            }
    }

    fun increaseQuantity( documentId: String, onResult: (String?, Exception?) -> Unit ) {

        // runTransaction --> Get data and then we can update on this data
        firestore.runTransaction {transaction ->
            val documentRef = cartCollection.document(documentId)
            val document = transaction.get(documentRef)
            val productObject = document.toObject(CartProduct::class.java)

            productObject?.let{cartProduct ->
                var newQuantity = cartProduct.quantity + 1
                val newProductObject = cartProduct.copy(quantity = newQuantity) /// Copy the cartProduct and then change the "quantity" to "newQuantity"
                transaction.set( documentRef, newProductObject)
            }
        }.addOnSuccessListener {
            onResult( documentId, null )
        }.addOnFailureListener {
            onResult( null, it )
        }
    }

    fun decreaseQuantity( documentId: String, onResult: (String?, Exception?) -> Unit ) {

        // runTransaction --> Get data and then we can update on this data
        firestore.runTransaction {transaction ->
            val documentRef = cartCollection.document(documentId)
            val document = transaction.get(documentRef)
            val productObject = document.toObject(CartProduct::class.java)

            productObject?.let{cartProduct ->
                var newQuantity = cartProduct.quantity - 1
                val newProductObject = cartProduct.copy(quantity = newQuantity) /// Copy the cartProduct and then change the "quantity" to "newQuantity"
                transaction.set( documentRef, newProductObject)
            }
        }.addOnSuccessListener {
            onResult( documentId, null )
        }.addOnFailureListener {
            onResult( null, it )
        }
    }

    enum class QuantityChanging {
        INCREASE, DECREASE
    }
}
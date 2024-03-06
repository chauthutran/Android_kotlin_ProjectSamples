package com.psi.onlineshop.viewmodels

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.psi.onlineshop.data.Product
import com.psi.onlineshop.utils.Constants
import com.psi.onlineshop.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
//import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    application: Application
): AndroidViewModel(application) {

    private val _addNewProduct = MutableStateFlow<Resource<Product>>(Resource.UnSpecified())
    val addNewProduct = _addNewProduct.asStateFlow()

    fun saveProduct(product: Product, imagesByteArrays: List<ByteArray>) {
        viewModelScope.launch { _addNewProduct.emit( Resource.Loading() ) }


        viewModelScope.launch {
            // Save images
            imagesByteArrays.forEach { imageByteArray ->
//                val imageDirectory =storage.child("${Constants.PRODUCTS_COLLECTION}/${Constants.PRODUCT_IMAGES}/${UUID.randomUUID()}")
//                val result = imageDirectory.putBytes(imageByteArray).await()
//                val imageUrl = result.storage.downloadUrl.await().toString()
//                product.images.add(imageUrl)
            }

            // Save products
//            firestore.collection("${Constants.PRODUCTS_COLLECTION}").add(product)
//                .addOnSuccessListener {
//                    viewModelScope.launch { _addNewProduct.emit( Resource.Success(product)) }
//                }.addOnFailureListener {
//                    viewModelScope.launch { _addNewProduct.emit( Resource.Error(it.message.toString())) }
//                }
        }

    }



}
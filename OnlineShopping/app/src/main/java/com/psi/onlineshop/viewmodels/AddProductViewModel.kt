package com.psi.onlineshop.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.psi.onlineshop.data.Product
import com.psi.onlineshop.httpRequest.HttpRequest
import com.psi.onlineshop.httpRequest.HttpRequestConfig
import com.psi.onlineshop.httpRequest.HttpRequestUtil
import com.psi.onlineshop.httpRequest.Method
import com.psi.onlineshop.utils.Constants
import com.psi.onlineshop.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class AddProductViewModel (
    application: Application
): AndroidViewModel(application) {

    private val _addNewProduct = MutableStateFlow<Resource<Product>>(Resource.UnSpecified())
    val addNewProduct = _addNewProduct.asStateFlow()

    fun saveProduct(product: Product, imagesByteArrays: List<ByteArray>) {
//    fun saveProduct(product: Product, imageUris: List<Uri>) {
        viewModelScope.launch { _addNewProduct.emit( Resource.Loading() ) }


        viewModelScope.launch {
            // Save images
//            imageUris.forEach { imageUri ->
//
//
//            }

            // Save products
            var payload = JSONObject()
            payload.put("payload", HttpRequestUtil.convertObjToJson(product))
            payload.put("collectionName", HttpRequestConfig.COLLECTION_PRODUCTS)

            val request = HttpRequest(
                method = Method.POST,
                parameters = mapOf("action" to HttpRequestConfig.REQUEST_ACTION_ADD_ONE),
                postedData = payload.toString()
            )
            request.json<Product>{ result, response ->
                if( response.error != null )
                {
                    val message = response.error?.getString("message") ?: ""
                    viewModelScope.launch { _addNewProduct.emit(Resource.Error(message)) }
                }
                else if(result != null && result is List<*>)
                {
                    viewModelScope.launch { _addNewProduct.emit(Resource.Success(result.get(0) as Product)) }
                }
            }


        }

    }

//    fun uploadImages( imagesByteArrays: List<ByteArray> )
//    {
//        var payload = JSONObject()
//        payload.put("payload", HttpRequestUtil.convertObjToJson(product))
//        payload.put("collectionName", "products")
////
//
//        imagesByteArrays.forEach { imageByteArray ->
//
////                var payload = JSONObject()
////                payload.put("payload", {
////
////                })
////                payload.put("collectionName", "products")
//
//            val request = HttpRequest(
//                path="upload",
//                method = Method.POST,
//                parameters = mapOf("action" to HttpRequestConfig.REQUEST_ACTION_ADD_ONE),
//
////                postedData = payload.toString()
////        )
//        }
//    }

//    fun uploadImage( uri: Uri ){
//        // Save products
//        var payload = JSONObject()
//        payload.put("payload", HttpRequestUtil.convertObjToJson(product))
//        payload.put("collectionName", "products")
//
//        val request = HttpRequest(
//            method = Method.POST,
//            parameters = mapOf("action" to HttpRequestConfig.REQUEST_ACTION_ADD_ONE),
//            postedData = payload.toString()
//        )
//        request.json<Product>{ result, response ->
//            if( response.error != null )
//            {
//                val message = response.error?.getString("message") ?: ""
//                viewModelScope.launch { _addNewProduct.emit(Resource.Error(message)) }
//            }
//            else if(result != null && result is List<*>)
//            {
//                viewModelScope.launch { _addNewProduct.emit(Resource.Success(result.get(0) as Product)) }
//            }
//        }
//    }

//    fun saveProduct(product: Product, imagesByteArrays: List<ByteArray>) {
//        viewModelScope.launch { _addNewProduct.emit( Resource.Loading() ) }
//
//
//        viewModelScope.launch {
//            // Save images
//            imagesByteArrays.forEach { imageByteArray ->
////                val imageDirectory =storage.child("${Constants.PRODUCTS_COLLECTION}/${Constants.PRODUCT_IMAGES}/${UUID.randomUUID()}")
////                val result = imageDirectory.putBytes(imageByteArray).await()
////                val imageUrl = result.storage.downloadUrl.await().toString()
////                product.images.add(imageUrl)
//            }
//
//            // Save products
//            var payload = JSONObject()
//            payload.put("payload", HttpRequestUtil.convertObjToJson(product))
//            payload.put("collectionName", "products")
//
//            val request = HttpRequest(
//                method = Method.POST,
//                parameters = mapOf("action" to HttpRequestConfig.REQUEST_ACTION_ADD_ONE),
//                postedData = payload.toString()
//            )
//            request.json<Product>{ result, response ->
//                if( response.error != null )
//                {
//                    val message = response.error?.getString("message") ?: ""
//                    viewModelScope.launch { _addNewProduct.emit(Resource.Error(message)) }
//                }
//                else if(result != null && result is List<*>)
//                {
//                    viewModelScope.launch { _addNewProduct.emit(Resource.Success(result.get(0) as Product)) }
//                }
//            }
//
//
//        }
//
//    }



}
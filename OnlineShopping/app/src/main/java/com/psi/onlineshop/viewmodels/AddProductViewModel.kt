package com.psi.onlineshop.viewmodels

import android.app.Application
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.psi.onlineshop.data.Product
import com.psi.onlineshop.data.User
import com.psi.onlineshop.httpRequest.FileDataPart
import com.psi.onlineshop.httpRequest.HttpRequestConfig
import com.psi.onlineshop.httpRequest.HttpRequestUtil
import com.psi.onlineshop.httpRequest.VolleyFileUploadRequest
import com.psi.onlineshop.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.UUID

class AddProductViewModel (
    application: Application
): AndroidViewModel(application) {

    val context = getApplication<Application>().applicationContext


    private val _addNewProduct = MutableStateFlow<Resource<Product>>(Resource.UnSpecified())
    val addNewProduct = _addNewProduct.asStateFlow()

    var imageFileIds = ArrayList<String>()
    var progressingImgIdx = 0
    var totalImg = 0

    fun saveProduct(product: Product, imagesByteArrays: MutableMap<String,ByteArray>) {

//    fun saveProduct(product: Product, imageUris: List<Uri>) {
        viewModelScope.launch { _addNewProduct.emit( Resource.Loading() ) }


        viewModelScope.launch {

            imageFileIds = ArrayList<String>()
            progressingImgIdx = 0
            totalImg = imagesByteArrays.size

            // saveImages
            uploadImages(imagesByteArrays) { fileIds ->
                // Add Product
                addProduct( product ) { response ->
                    var status = response.getString("status")
                    var data = response.getJSONArray("data")
                    if( status == HttpRequestConfig.RESPONSE_STATUS_SUCCESS && data.length() > 0 ) {
                        val product = HttpRequestUtil.convertJsonToObj<Product>(data.getJSONObject(0))
                        viewModelScope.launch { _addNewProduct.emit(Resource.Success(product)) }
                    }
                    else {
                        val message = response.getString("status")
                        viewModelScope.launch { _addNewProduct.emit(Resource.Error(message)) }
                    }
                }
            }

        }

    }


    private inline fun uploadImages(imagesByteArrays: MutableMap<String,ByteArray>, crossinline completion: (ArrayList<String>) -> Unit )  {

        imagesByteArrays.forEach {
            val imgName = it.key
            val imgData = it.value

            HttpRequestUtil.uploadImage(context, imgName, imgData ) { response ->
                var status = response.getString("status")
                if( status == HttpRequestConfig.RESPONSE_STATUS_SUCCESS ) {
                    var fileName = response.getJSONObject("data").getString("filename")
                    imageFileIds.add(fileName)
                }

                progressingImgIdx++
                if( progressingImgIdx == totalImg ) {
                    completion(imageFileIds)
                }
            }
        }

    }

    private inline fun addProduct(product: Product, crossinline completion: (JSONObject) -> Unit ) {

        var payload = HttpRequestUtil.convertObjToJson(product)

        HttpRequestUtil.sendPOSTRequest(context, HttpRequestConfig.REQUEST_ACTION_ADD_ONE, HttpRequestConfig.COLLECTION_PRODUCTS, payload) { response ->
            completion( response )
        }
    }
}
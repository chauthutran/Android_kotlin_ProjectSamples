package com.psi.onlineshop.viewmodels

import android.app.Application
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

    private val context = getApplication<Application>().applicationContext


    private val _addNewProduct = MutableStateFlow<Resource<Product>>(Resource.UnSpecified())
    val addNewProduct = _addNewProduct.asStateFlow()

    fun saveProduct(product: Product, imagesByteArrays: List<ByteArray>) {

//    fun saveProduct(product: Product, imageUris: List<Uri>) {
        viewModelScope.launch { _addNewProduct.emit( Resource.Loading() ) }


        viewModelScope.launch {

            // Generate the image name
            val imgNameList = createImageNames(imagesByteArrays.size, product.name)

            // saveImages
            uploadImages(imagesByteArrays, imgNameList)

            // Save products
            product.images = imgNameList
            var payload = HttpRequestUtil.convertObjToJson(product)

            HttpRequestUtil.sendPOSTRequest(context, HttpRequestConfig.REQUEST_ACTION_ADD_ONE, HttpRequestConfig.COLLECTION_PRODUCTS, payload) { response ->
                if( response is JSONObject)
                {
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


        }

    }

    private fun createImageNames ( count: Int, productName: String ): ArrayList<String>
    {
        var imgNameList = ArrayList<String>()
        (0 until count).forEach {
            imgNameList.add("${productName}-${UUID.randomUUID()}-img")
        }
        return imgNameList;
    }
    fun uploadImages( imagesByteArrays: List<ByteArray>, imgNameList: ArrayList<String> ) {
        (0 until imagesByteArrays.size).forEach {
            val imageData = imagesByteArrays.get(it)

            val queue = Volley.newRequestQueue(getApplication<Application>().applicationContext)
            val url = "${HttpRequestConfig.BASE_URL_MONGODB_SERVICE}/upload"

            val stringRequest = object : VolleyFileUploadRequest(
                Method.POST,
                url,
                Response.Listener { response ->
                    println("-------------------- response : $response")
                    //                        if( response == "success") {
                    //                            Toast.makeText(applicationContext, "Image uploaded", Toast.LENGTH_SHORT).show()
                    //                        }
                    //                        else {
                    //                            Toast.makeText(applicationContext, "Fail to upload image", Toast.LENGTH_SHORT).show()
                    //                        }

                },
                Response.ErrorListener {
                    println("-------------------- response : $it.message")
                    //                Toast.makeText(, it.message, Toast.LENGTH_SHORT).show()
                }
            ) {

                override fun getByteData(): MutableMap<String, FileDataPart> {
                    var params = HashMap<String, FileDataPart>()

                    params.put("file", FileDataPart("${imgNameList.get(it)}", imageData!!, "jpg"))

                    return params
                }
            }
            queue.add(stringRequest)
        }

    }

}
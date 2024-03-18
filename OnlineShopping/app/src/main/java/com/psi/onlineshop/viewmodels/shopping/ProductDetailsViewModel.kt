package com.psi.onlineshop.viewmodels.shopping

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.psi.onlineshop.ShoppingApplication
import com.psi.onlineshop.activities.ShoppingActivity
import com.psi.onlineshop.data.Product
import com.psi.onlineshop.data.ProductLike
import com.psi.onlineshop.data.User
import com.psi.onlineshop.httpRequest.HttpRequestConfig
import com.psi.onlineshop.httpRequest.HttpRequestUtil
import com.psi.onlineshop.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.UUID

//import org.bson.types.ObjectId;


class ProductDetailsViewModel(
    application: Application
): AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    private val _liked = MutableStateFlow<Resource<ProductLike>>(Resource.UnSpecified())
    val liked = _liked.asStateFlow()

    private val _unliked = MutableStateFlow<Resource<Boolean>>(Resource.UnSpecified())
    val unliked = _unliked.asStateFlow()


    fun setLike(product: Product) {
        viewModelScope.launch { _liked.emit( Resource.Loading() ) }

        viewModelScope.launch {
            val userData = ShoppingApplication.getUserData(context)
            val productLike = ProductLike( userId = userData!!.id!!, productId = product!!.id!! )

            HttpRequestUtil.sendPOSTRequest(context, HttpRequestConfig.REQUEST_ACTION_ADD_ONE, HttpRequestConfig.COLLECTION_PRODUCT_LIKES, HttpRequestUtil.convertObjToJson(productLike)) { response ->
                if( response is JSONObject)
                {
                    var status = response.getString("status")
                    if( status == HttpRequestConfig.RESPONSE_STATUS_SUCCESS ) {
                        var data = response.getJSONArray("data")
                        if( data.length() > 0 ) {
                            val prodLike = HttpRequestUtil.convertJsonToObj<ProductLike>(data.getJSONObject(0))
                            viewModelScope.launch { _liked.emit( Resource.Success(prodLike)) }
                        }
                    }
                    else {
                        val message = response.getString("data")
                        viewModelScope.launch { _liked.emit( Resource.Error(message)) }
                    }
                }
            }


        }
    }

    fun setUnlike(id: String) {
        viewModelScope.launch { _unliked.emit( Resource.Loading() ) }

        viewModelScope.launch {
            val payload = JSONObject()
            payload.put("id", id)

            HttpRequestUtil.sendPOSTRequest(context,HttpRequestConfig.REQUEST_ACTION_DELETE, HttpRequestConfig.COLLECTION_PRODUCT_LIKES, payload) { response ->
                if( response is JSONObject)
                {
                    var status = response.getString("status")
                    if( status == HttpRequestConfig.RESPONSE_STATUS_SUCCESS ) {
                        var data = response.getJSONArray("data")
                        if( data.length() > 0 ) {
                            viewModelScope.launch { _unliked.emit( Resource.Success(true)) }
                        }
                    }
                    else {
                        val message = response.getString("data")
                        viewModelScope.launch { _unliked.emit( Resource.Error(message)) }
                    }
                }
            }


        }
    }

    fun getLike(product: Product, state: (ProductLike?) -> Unit) {

        viewModelScope.launch {
            val userData = ShoppingApplication.getUserData(context)

            var searchConditions = JSONObject()
            searchConditions.put("userId", userData!!.id!!)
            searchConditions.put("productId", product!!.id!!)

            HttpRequestUtil.sendPOSTRequest(context, HttpRequestConfig.REQUEST_ACTION_FIND, HttpRequestConfig.COLLECTION_PRODUCT_LIKES, searchConditions) { response ->
                if( response is JSONObject)
                {
                    var status = response.getString("status")
                    if( status == HttpRequestConfig.RESPONSE_STATUS_SUCCESS ) {
                        var data = response.getJSONArray("data")
                        if(  data.length() > 0 ) {
                            val productLike = HttpRequestUtil.convertJsonToObj<ProductLike>(data.getJSONObject(0))
                            state(productLike)
                        }
                        else {
                            state(null)
                        }
                    }

                }
            }


        }
    }

}
package com.psi.onlineshop.viewmodels.shopping

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.psi.onlineshop.data.Product
import com.psi.onlineshop.httpRequest.HttpRequestConfig
import com.psi.onlineshop.httpRequest.HttpRequestUtil
import com.psi.onlineshop.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class HomeViewModel (
    application: Application
): AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext


    private val _todayProposals = MutableStateFlow<Resource<List<Product>>>(Resource.UnSpecified())
    val todayProposals: StateFlow<Resource<List<Product>>> = _todayProposals


    init {
        fetchTodayProposals()
    }

    private fun fetchTodayProposals() {
        viewModelScope.launch {
            _todayProposals.emit(Resource.Loading())
        }

        viewModelScope.launch {
            HttpRequestUtil.sendPOSTRequest(context, HttpRequestConfig.REQUEST_ACTION_FIND, HttpRequestConfig.COLLECTION_PRODUCTS, JSONObject()) { response ->
                println("============================== response : ${response}")
                if( response is JSONObject)
                {
                    var status = response.getString("status")
                    var data = response.getJSONArray("data")

                    if( status == HttpRequestConfig.RESPONSE_STATUS_SUCCESS && data.length() > 0 ) {
                        val products = HttpRequestUtil.convertJsonArrToListObj<Product>(data)
                        viewModelScope.launch { _todayProposals.emit(Resource.Success(products)) }
                    }
                    else {
                        val message = response.getString("status")
                        viewModelScope.launch { _todayProposals.emit(Resource.Error(message)) }
                    }
                }
            }

        }

    }


    private fun getLike() {

    }
}
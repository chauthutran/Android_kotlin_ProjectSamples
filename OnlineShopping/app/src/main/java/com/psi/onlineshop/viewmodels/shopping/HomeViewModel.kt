package com.psi.onlineshop.viewmodels.shopping

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.psi.onlineshop.data.Product
import com.psi.onlineshop.data.User
import com.psi.onlineshop.httpRequest.HttpRequest
import com.psi.onlineshop.httpRequest.HttpRequestConfig
import com.psi.onlineshop.httpRequest.Method
import com.psi.onlineshop.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class HomeViewModel (
    application: Application
): AndroidViewModel(application) {

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

//            var searchConditions = JSONObject()
//            searchConditions.put("name", "")

            var searchData = JSONObject()
//            searchData.put("payload", searchConditions)
            searchData.put("collectionName", HttpRequestConfig.COLLECTION_PRODUCTS)

            val request = HttpRequest(
                method = Method.POST,
                parameters = mapOf("action" to HttpRequestConfig.REQUEST_ACTION_FIND),
                postedData = searchData.toString()
            )
            request.json<Product> { result, response ->
                println("=======================================================================")
                println(result)
                if( response.error != null )
                {
                    val message = response.error?.getString("message") ?: ""
                    viewModelScope.launch { _todayProposals.emit(Resource.Error(message)) }
                }
                else if(result != null )
                {
                    viewModelScope.launch { _todayProposals.emit(Resource.Success(result as List<Product> )) }
                }
            }

        }

    }
}
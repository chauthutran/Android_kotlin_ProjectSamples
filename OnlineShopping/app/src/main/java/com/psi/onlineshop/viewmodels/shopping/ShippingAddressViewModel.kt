package com.psi.onlineshop.viewmodels.shopping

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.psi.onlineshop.ShoppingApplication
import com.psi.onlineshop.data.ShippingAddress
import com.psi.onlineshop.data.User
import com.psi.onlineshop.httpRequest.HttpRequestConfig
import com.psi.onlineshop.httpRequest.HttpRequestUtil
import com.psi.onlineshop.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class ShippingAddressViewModel (
    application: Application
): AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    private val _addNewAddressStatus = MutableStateFlow<Resource<User>>(Resource.UnSpecified())
    val addNewAddressStatus = _addNewAddressStatus.asStateFlow()


    fun addAddress(address: ShippingAddress) {
        val validate = validateInputs( address )

        if( validate ) {
            viewModelScope.launch { _addNewAddressStatus.emit( Resource.Loading() ) }

            viewModelScope.launch {

                val userData = ShoppingApplication.getUserData(context)
                userData!!.shippingAddresses.add(address)

                var payload = HttpRequestUtil.convertObjToJson(address)

                HttpRequestUtil.sendPOSTRequest(
                    context,
                    HttpRequestConfig.REQUEST_ACTION_UPDATE,
                    HttpRequestConfig.COLLECTION_USERS,
                    payload
                ) { response ->
                    if (response is JSONObject) {
                        var status = response.getString("status")
                        var data = response.getJSONArray("data")
                        if (status == HttpRequestConfig.RESPONSE_STATUS_SUCCESS && data.length() > 0) {
                            val user = HttpRequestUtil.convertJsonToObj<User>(data.getJSONObject(0))
                            viewModelScope.launch { _addNewAddressStatus.emit(Resource.Success(user)) }
                        } else {
                            val message = response.getString("data")
                            viewModelScope.launch { _addNewAddressStatus.emit(Resource.Error(message)) }
                        }
                    }
                }
            }
        }
        else
        {
            viewModelScope.launch { _addNewAddressStatus.emit( Resource.Error("All fields are required" ) ) }
        }
    }

    private fun validateInputs( address: ShippingAddress ): Boolean {
        return address.addressTitle.trim().isNotEmpty()
                && address.fullName.trim().isNotEmpty()
                && address.phone.trim().isNotEmpty()
                && address.street.trim().isNotEmpty()
                && address.state.trim().isNotEmpty()
                && address.city.trim().isNotEmpty()
    }
}
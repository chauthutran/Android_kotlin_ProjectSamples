package com.psi.shoppingapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.psi.shoppingapp.data.Product
import com.psi.shoppingapp.utils.Constants
import com.psi.shoppingapp.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
): ViewModel() {

    private val _specialProducts = MutableStateFlow<Resource<List<Product>>>(Resource.UnSpecified())
    val specialProducts: StateFlow<Resource<List<Product>>> = _specialProducts

    private val _bestDeals = MutableStateFlow<Resource<List<Product>>>(Resource.UnSpecified())
    val bestDeals: StateFlow<Resource<List<Product>>> = _bestDeals

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.UnSpecified())
    val bestProducts: StateFlow<Resource<List<Product>>> = _bestProducts


    init {
        fetchSpecialProducts()
        fetchBestDeals()
        fetchBestProducts()
    }

    private fun fetchSpecialProducts() {
        viewModelScope.launch {
            _specialProducts.emit(Resource.Loading())
        }

        firestore.collection(Constants.PRODUCTS_COLLECTION)
            .whereEqualTo("category", "Special Product")
            .get()
            .addOnSuccessListener { result ->
                var specialProductList = result.toObjects(Product::class.java)

                viewModelScope.launch {
                    _specialProducts.emit(Resource.Success(specialProductList))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _specialProducts.emit(Resource.Error(it.message.toString()))

                }
            }
    }

    private fun fetchBestDeals() {
        viewModelScope.launch {
            _bestDeals.emit(Resource.Loading())
        }

        firestore.collection(Constants.PRODUCTS_COLLECTION)
            .whereEqualTo("category", "Best Deal")
            .get()
            .addOnSuccessListener { result ->
                var bestDealList = result.toObjects(Product::class.java)
                println("=== bestDealList: ${bestDealList.size}")
                viewModelScope.launch {
                    _bestDeals.emit(Resource.Success(bestDealList))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _bestDeals.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    private fun fetchBestProducts() {
        viewModelScope.launch {
            _bestProducts.emit(Resource.Loading())
        }

        firestore.collection(Constants.PRODUCTS_COLLECTION)
            .whereEqualTo("category", "Best Product")
            .get()
            .addOnSuccessListener { result ->
                var bestProductList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Success(bestProductList))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}
package com.psi.shoppingapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.psi.shoppingapp.data.Category
import com.psi.shoppingapp.data.Product
import com.psi.shoppingapp.utils.Constants
import com.psi.shoppingapp.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel constructor(
    private val firestore: FirebaseFirestore,
    private val category: Category
): ViewModel() {

    private val _offerProducts = MutableStateFlow<Resource<List<Product>>>(Resource.UnSpecified())
    val offerProducts = _offerProducts.asStateFlow()

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.UnSpecified())
    val bestProducts = _bestProducts.asStateFlow()

    private val pagingInfo = PagingInfo()

    init {
        fetchOfferProducts()
        fetchBestProducts()
    }
    
    fun fetchOfferProducts() {
        viewModelScope.launch {
            _offerProducts.emit(Resource.Loading())
        }

        firestore.collection(Constants.PRODUCTS_COLLECTION)
            .whereEqualTo("category", category.category)
            .whereNotEqualTo("offerPercentage", null)
            .get()
            .addOnSuccessListener {
                val products = it.toObjects(Product::class.java)

                viewModelScope.launch {
                    _offerProducts.emit(Resource.Success(products))
                }


            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _offerProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun fetchBestProducts() {
        if (!pagingInfo.isPagingEnd) {
            viewModelScope.launch {
                _bestProducts.emit(Resource.Loading())
            }
            firestore.collection(Constants.PRODUCTS_COLLECTION)
                .whereEqualTo("category", category.category)
                .whereEqualTo("offerPercentage", null)
                .limit(pagingInfo.productsPage * 10)
                .get()
                .addOnSuccessListener {
                    val products = it.toObjects(Product::class.java)
                    pagingInfo.isPagingEnd = (products == pagingInfo.oldProducts)
                    pagingInfo.oldProducts = products

                    viewModelScope.launch {
                        _bestProducts.emit(Resource.Success(products))
                    }

                    pagingInfo.productsPage++
                }
                .addOnFailureListener {
                    viewModelScope.launch {
                        _bestProducts.emit(Resource.Error(it.message.toString()))
                    }
                }
        }
    }

}
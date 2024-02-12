package com.psi.shoppingapp.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.psi.shoppingapp.data.Category
import com.psi.shoppingapp.viewmodels.CategoryViewModel

class BaseCategoryViewModelFactoryFactory(
    private val firestore: FirebaseFirestore,
    private val category: Category
): ViewModelProvider.Factory {

    override fun<T: ViewModel> create(modelClass: Class<T>): T {
        return CategoryViewModel( firestore, category ) as T
    }
}
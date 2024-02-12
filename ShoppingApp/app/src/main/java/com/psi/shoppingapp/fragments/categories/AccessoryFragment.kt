package com.psi.shoppingapp.fragments.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.psi.shoppingapp.R
import com.psi.shoppingapp.data.Category
import com.psi.shoppingapp.utils.Resource
import com.psi.shoppingapp.viewmodels.CategoryViewModel
import com.psi.shoppingapp.viewmodels.factory.BaseCategoryViewModelFactoryFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class AccessoryFragment : BaseCategoryFragment() {
    @Inject
    lateinit var firestore: FirebaseFirestore

    val viewModel by viewModels<CategoryViewModel> {
        BaseCategoryViewModelFactoryFactory( firestore, Category.Accessory)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.offerProducts.collectLatest {
                when(it) {
                    is Resource.Loading -> {
                        showOfferProductsLoading()
                    }
                    is Resource.Success -> {
                        offerAdapter.differ.submitList(it.data)
                        hideOfferProductsLoading()
                    }
                    is Resource.Error -> {
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG).show()
                        hideOfferProductsLoading()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bestProducts.collectLatest {
                when(it) {
                    is Resource.Loading -> {
                        showBestProductsLoading()
                    }
                    is Resource.Success -> {
                        bestProductsAdapter.differ.submitList(it.data)
                        hideBestProductsLoading()
                    }
                    is Resource.Error -> {
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG).show()
                        hideBestProductsLoading()
                    }
                    else -> Unit
                }
            }
        }

    }

    override fun onOfferProductsPagingRequest() {
        super.onOfferProductsPagingRequest()
    }

    override fun onBestProductsPagingRequest() {
        super.onBestProductsPagingRequest()
    }
}
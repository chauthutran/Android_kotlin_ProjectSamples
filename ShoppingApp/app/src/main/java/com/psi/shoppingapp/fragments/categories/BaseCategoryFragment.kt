
package com.psi.shoppingapp.fragments.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.psi.shoppingapp.R
import com.psi.shoppingapp.adapters.BestProductsAdapter
import com.psi.shoppingapp.databinding.FragmentBaseCategoryBinding
import dagger.hilt.android.AndroidEntryPoint


open class BaseCategoryFragment : Fragment(R.layout.fragment_base_category) {

    private lateinit var binding: FragmentBaseCategoryBinding
    protected val offerAdapter: BestProductsAdapter by lazy { BestProductsAdapter() }
    protected val bestProductsAdapter: BestProductsAdapter by lazy { BestProductsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOfferRv()
        setupBestProductsRv()

        binding.rvOfferProducts.addOnScrollListener(( object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if( !recyclerView.canScrollVertically(1) && dx != 0 ) {
                    onOfferProductsPagingRequest()
                }
            }
        } ))

        binding.nestedScrollBaseCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{ scrollView,_, scrollY, _,_ ->
            if( scrollView.getChildAt(0).bottom <= scrollView.height + scrollY) {
                onBestProductsPagingRequest()
            }
        })

    }

    open fun onOfferProductsPagingRequest() {

    }

    open fun onBestProductsPagingRequest() {

    }

    fun showOfferProductsLoading() {
        binding.offerProductsProgressBar.visibility = View.VISIBLE
    }

    fun hideOfferProductsLoading() {
        binding.offerProductsProgressBar.visibility = View.GONE
    }

    fun showBestProductsLoading() {
        binding.bestProductsProgressBar.visibility = View.VISIBLE
    }

    fun hideBestProductsLoading() {
        binding.bestProductsProgressBar.visibility = View.GONE
    }


    private fun setupBestProductsRv() {
        binding.rvBestProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductsAdapter
        }
    }

    private fun setupOfferRv() {
        binding.rvOfferProducts.apply {
            layoutManager = LinearLayoutManager( requireContext(), LinearLayoutManager.HORIZONTAL, false)

            adapter = offerAdapter
        }
    }
}
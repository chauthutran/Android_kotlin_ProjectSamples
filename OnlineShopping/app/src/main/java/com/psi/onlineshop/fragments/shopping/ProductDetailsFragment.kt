package com.psi.onlineshop.fragments.shopping

import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.psi.onlineshop.adapters.ProductColorsAdapter
import com.psi.onlineshop.adapters.ProductSizesAdapter
import com.psi.onlineshop.databinding.FragmentProductDetailsBinding
import com.psi.onlineshop.utils.formatNumber
import com.psi.onlineshop.utils.getOfferPercentagePrice
import com.psi.onlineshop.utils.getPercentage
import com.psi.onlineshop.utils.setupSliderImages

class ProductDetailsFragment : Fragment() {

    private val args by navArgs<ProductDetailsFragmentArgs>()

    private lateinit var binding: FragmentProductDetailsBinding

    private val sizesAdapter by lazy { ProductSizesAdapter() }

    private val colorsAdapter by lazy { ProductColorsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        binding.apply {
            productSliderView.setupSliderImages(product.imgFileIds, false)

            tvProductName.text = product.name
            tvProductDescription.text = product.description

            if( product.offerPercentage != null) {
                tvProductOfferPercentage.text = "${product.offerPercentage.getPercentage()}%"
                tvProductPrice.text = "$ ${product.price.formatNumber()}"
                tvProductPrice.paintFlags = tvProductPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                tvProductRealPrice.text = "$ ${product.price.getOfferPercentagePrice(product.offerPercentage).formatNumber()}"
            }
            else {
                tvProductOfferPercentage.visibility = View.GONE
                tvProductPrice.visibility = View.GONE

                tvProductRealPrice.text = "$ ${product.price.formatNumber()}"
            }


            if( product.colors.isNullOrEmpty() ) {
                tvProductColors.visibility = View.GONE
                rvColors.visibility = View.GONE
                tvProductSelectedColor.visibility = View.GONE
            }
            else {
                rvColors.apply {
                    adapter = colorsAdapter
                    layoutManager = LinearLayoutManager(requireContext())
                }
            }

            if( product.sizes.isNullOrEmpty() ) {
                tvProductSize.visibility = View.GONE
                rvSizes.visibility = View.GONE
                tvProductSelectedSize.visibility = View.GONE
            }
            else {
                rvSizes.apply {
                    adapter = sizesAdapter
                    layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                }
            }
        }

        product.colors?.let { colorsAdapter.differ.submitList(it) }
        product.sizes?.let { sizesAdapter.differ.submitList(it) }

    }
}
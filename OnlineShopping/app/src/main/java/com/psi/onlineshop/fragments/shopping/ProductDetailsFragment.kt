package com.psi.onlineshop.fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.psi.onlineshop.adapters.ProductColorsAdapter
import com.psi.onlineshop.adapters.ProductSizesAdapter
import com.psi.onlineshop.databinding.FragmentProductDetailsBinding

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
            tvProductName.text = product.name
            tvProductPrice.text = "$ ${product.price}"
            tvProductDescription.text = product.description

            if( product.colors.isNullOrEmpty() ) {
                binding.tvProductColors.visibility = View.GONE
            }

            if( product.sizes.isNullOrEmpty() ) {
                binding.tvProductSize.visibility = View.GONE
            }
        }
    }
}
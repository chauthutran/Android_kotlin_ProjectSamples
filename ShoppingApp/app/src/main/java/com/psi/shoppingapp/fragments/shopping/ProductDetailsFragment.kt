package com.psi.shoppingapp.fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.psi.shoppingapp.R
import com.psi.shoppingapp.activities.ShoppingActivity
import com.psi.shoppingapp.adapters.ColorsAdapter
import com.psi.shoppingapp.adapters.SizesAdapter
import com.psi.shoppingapp.adapters.ViewPager2ImagesAdapter
import com.psi.shoppingapp.databinding.FragmentProductDetailsBinding
import com.psi.shoppingapp.utils.hideBottomNavigationView

class ProductDetailsFragment : Fragment() {

    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var binding: FragmentProductDetailsBinding

    private val viewPagerAdapter by lazy {ViewPager2ImagesAdapter()}
    private val sizesAdapter by lazy { SizesAdapter() }
    private val colorsAdapter by lazy {ColorsAdapter()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideBottomNavigationView()

        binding = FragmentProductDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        setupSizesRv()
        setupColorsRv()
        setupViewPagers()



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

        viewPagerAdapter.differ.submitList(product.images)
        product.colors?.let { colorsAdapter.differ.submitList(it) }
        product.sizes?.let { sizesAdapter.differ.submitList(it) }

        binding.imageClose.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupViewPagers() {
        binding.apply {
            viewPagerProductImages.adapter = viewPagerAdapter
        }
    }

    private fun setupColorsRv() {
        binding.rvColors.apply {
            adapter = colorsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupSizesRv() {
        binding.rvSizes.apply {
            adapter = sizesAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }
}
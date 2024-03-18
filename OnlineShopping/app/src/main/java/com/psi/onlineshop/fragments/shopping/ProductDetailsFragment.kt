package com.psi.onlineshop.fragments.shopping

import android.content.res.ColorStateList
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.psi.onlineshop.R
import com.psi.onlineshop.adapters.ProductColorsAdapter
import com.psi.onlineshop.adapters.ProductSizesAdapter
import com.psi.onlineshop.data.Product
import com.psi.onlineshop.data.ProductLike
import com.psi.onlineshop.data.ProductVariant
import com.psi.onlineshop.databinding.FragmentProductDetailsBinding
import com.psi.onlineshop.httpRequest.HttpRequestConfig
import com.psi.onlineshop.utils.Resource
import com.psi.onlineshop.utils.SpacesItemDecoration
import com.psi.onlineshop.utils.formatNumber
import com.psi.onlineshop.utils.getItem
import com.psi.onlineshop.utils.getOfferPercentagePrice
import com.psi.onlineshop.utils.getPercentage
import com.psi.onlineshop.utils.hideBottomNavigationView
import com.psi.onlineshop.utils.setupSliderImages
import com.psi.onlineshop.viewmodels.shopping.ProductDetailsViewModel
import kotlinx.coroutines.launch
import java.util.Collections

class ProductDetailsFragment : Fragment() {

    private val args by navArgs<ProductDetailsFragmentArgs>()

    private lateinit var binding: FragmentProductDetailsBinding
    private lateinit var product: Product

    private val sizesAdapter by lazy { ProductSizesAdapter() }
    private val colorsAdapter by lazy { ProductColorsAdapter() }

    private var selectedSize: String? = null
    private var selectedColor: Int? = null
    private var productLike: ProductLike? = null

    private val productDetailsViewModel by viewModels<ProductDetailsViewModel>()

    private lateinit var likeMenuItem: MenuItem


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailsBinding.inflate(inflater)

        // Add toolbar in the top of the Fragment
        hideBottomNavigationView()
       // Add new toolbar with menu for the Fragment Details
        val toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)

        toolbar.setNavigationOnClickListener {
            // Handle navigation icon click (e.g., navigate back)
            requireActivity().onBackPressed()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBottomNavigation()

        product = args.product

        binding.apply {

            val imageList = getImageList(product)

            productSliderView.setupSliderImages("${HttpRequestConfig.BASE_URL_MONGODB_SERVICE}/file/", imageList, false)
            tvProductName.text = product.name
            tvProductDescription.text = product.description

            // For Colors
            generateColorList(product)

            // Selected first variant
            displaySelectedVariant(product, product.variants[0])

            binding.rvSizes.apply {
                adapter = sizesAdapter
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }
        }


        sizesAdapter.onItemClick = {
            selectedSize = it
            binding.tvProductSelectedSize.text = it
        }

        colorsAdapter.onItemClick = {
            selectedColor = it.color
            binding.tvProductSelectedColor.backgroundTintList = ColorStateList.valueOf(it.color!!)
            displaySelectedVariant( product, it )
        }

        // Get like if any
        productDetailsViewModel.getLike(product) { state ->
            productLike = state

            productLike?.let {
                likeMenuItem.setIcon(R.drawable.ic_heart_red_35);
            }
        }

        lifecycleScope.launch{
            productDetailsViewModel.liked.collect { it ->
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbar.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressbar.visibility = View.GONE
                        productLike = it.data
                        likeMenuItem.setIcon(R.drawable.ic_heart_red_35)
                    }

                    is Resource.Error -> {
                        binding.progressbar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launch{
            productDetailsViewModel.unliked.collect { it ->
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbar.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressbar.visibility = View.GONE
                        likeMenuItem.setIcon(R.drawable.ic_heart_35)
                    }

                    is Resource.Error -> {
                        binding.progressbar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.product_details_top_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.searchFragment -> {
                findNavController().navigate(R.id.action_productDetailsFragment_to_searchFragment)
                true
            }
            R.id.homeFragment -> {
                findNavController().navigate(R.id.action_productDetailsFragment_to_homeFragment)
                true
            }
            R.id.cartFragment -> {
                findNavController().navigate(R.id.action_productDetailsFragment_to_cartFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUpBottomNavigation() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_product_details)


        likeMenuItem = bottomNavigationView.menu.findItem(R.id.setFavorite)


        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.setFavorite -> {
                    setLikeToggle()
                    true
                }
                R.id.addToCart -> {
                    // Navigate to dashboard fragment or perform desired action
                    true
                }
                else -> false
            }
        }
    }

    private fun setLikeToggle() {
        if( productLike == null ) {
            productDetailsViewModel.setLike(product)
        }
        else {
            productDetailsViewModel.setUnlike(productLike!!.id)
        }
    }

    private fun getImageList(product: Product) : ArrayList<String> {
        return product.variants.map { it.imageName } as ArrayList<String>
    }

    private fun getColors(product: Product) : List<ProductVariant> {
        val colors = ( product.variants.map { it.color } as ArrayList<Int> ).distinct()
        val list = ArrayList<ProductVariant>()

        colors.forEach { color ->
            val filter = product.variants.filter { it.color == color }
            list.add(filter[0])
        }
        return list
    }

    private fun generateColorList (product: Product){
        val variantColorList = getColors(product)
        if( variantColorList.isEmpty() ) {
            binding.tvProductColors.visibility = View.GONE
            binding.tvProductSelectedColor.visibility = View.GONE
            binding.rvColors.visibility = View.GONE
        }
        else {
            colorsAdapter.differ.submitList(variantColorList)

//            // Select first color option
//            val firstColorItem: GradientDrawable = binding.rvColors[0].background as GradientDrawable
//            val colorBlue = resources.getColor(R.color.l_blue)
//            firstColorItem.setStroke( 6, colorBlue)
        }


        binding.rvColors.addItemDecoration( SpacesItemDecoration(30))
        binding.rvColors.apply {
            adapter = colorsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }


    private fun displaySelectedVariant( product: Product, productVariant: ProductVariant ){
        binding.apply {

            // Display Price
            if (productVariant.offerPercentage == null) {
                tvProductPrice.visibility = View.GONE
                tvProductOfferPercentage.visibility = View.GONE

                tvProductRealPrice.text = "$ ${productVariant.price.formatNumber()}"
            } else {
                tvProductPrice.text = "$ ${productVariant.price.formatNumber()}"
                tvProductOfferPercentage.text = "${productVariant.offerPercentage!!.getPercentage().formatNumber()}%"
                tvProductPrice.paintFlags = tvProductPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                val newPrice = productVariant.price.getOfferPercentagePrice(productVariant.offerPercentage!!)
                tvProductRealPrice.text = "$ ${newPrice.formatNumber()}"
            }

            // Display size list belong to the selected color
            tvProductSelectedSize.text = ""
            selectedColor?.let { color ->

                // Apply the new size list
                var sizeList = getSizesByColor(product, color)

                sizesAdapter.differ.submitList(sizeList)

                // Disable the sizes which doesn't have in this variant
                if( sizeList.size > 0 )
                {
                    tvProductSelectedSize.visibility = View.VISIBLE
                    tvProductSize.visibility = View.VISIBLE
                    rvSizes.visibility = View.VISIBLE
                }
                else {
                    tvProductSelectedSize.visibility = View.GONE
                    tvProductSize.visibility = View.GONE
                    rvSizes.visibility = View.GONE
                }

            }

        }

    }

    private fun getSizesByColor(product: Product, color: Int) : List<String> {
        var sizeList = ArrayList<String>()

        product.variants.forEach {
            if( it.size != null && it.color == color ){
                sizeList.add( it.size )
            }
        }
        (sizeList.distinct() as ArrayList<String>).sort()
        return (sizeList.distinct() as ArrayList<String>).toMutableList().sorted()
    }
}
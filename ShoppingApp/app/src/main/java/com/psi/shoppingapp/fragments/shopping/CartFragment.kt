package com.psi.shoppingapp.fragments.shopping

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.AlertDialogDefaults
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.psi.shoppingapp.R
import com.psi.shoppingapp.adapters.CartProductAdapter
import com.psi.shoppingapp.databinding.FragmentCartBinding
import com.psi.shoppingapp.fhirebase.FirebaseCommon
import com.psi.shoppingapp.utils.Resource
import com.psi.shoppingapp.utils.VerticalItemDecoration
import com.psi.shoppingapp.viewmodels.CartViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private val cartProductAdapter by lazy {CartProductAdapter()}

    // Reuse the viewModel from ShoppingActivity
    private val viewModel by activityViewModels<CartViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCartRv()

        lifecycleScope.launchWhenStarted {
            viewModel.productsPrice.collectLatest {price ->
                price?.let {
                    binding.tvTotalPrice.text = "$ ${price}"
                }
            }
        }

        binding.imageCloseCart.setOnClickListener {
            findNavController().navigateUp()
        }

        cartProductAdapter.onProductClick = {
            var b = Bundle().apply{ putParcelable("product", it.product) }
            findNavController().navigate(R.id.action_cartFragment_to_productDetailsFragment, b)
        }

        cartProductAdapter.onPlusClick = {
            viewModel.changeQuantity( it, FirebaseCommon.QuantityChanging.INCREASE )
        }

        cartProductAdapter.onMinusClick = {
            viewModel.changeQuantity( it, FirebaseCommon.QuantityChanging.DECREASE )
        }

        lifecycleScope.launchWhenStarted {
            viewModel.deleteDialog.collectLatest {
                val alertDialog = AlertDialog.Builder( requireContext() ).apply {
                    setTitle("Delete Item from cart")
                    setMessage("Do you want to delete item from the cart ?")
                    setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    setPositiveButton( "Yes" ) { dialog,_ ->
                         viewModel.deleteCartProduct(it)
                        dialog.dismiss()
                    }
                }

                alertDialog.create()
                alertDialog.show()
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.cartProducts.collectLatest {
                when( it ) {
                    is Resource.Loading -> {
                        binding.progressbarCart.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressbarCart.visibility = View.GONE

                        if( it.data!!.isEmpty() ) {
                            showEmptyCart()
                            hideOtherViews()
                        }
                        else {
                            hideEmptyCart()
                            showOtherViews()
                            cartProductAdapter.differ.submitList( it.data )
                        }
                    }
                    is Resource.Error -> {
                        binding.progressbarCart.visibility = View.GONE
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun showOtherViews() {
        binding.apply {
            rvCart.visibility = View.VISIBLE
            totalBoxContainer.visibility = View.VISIBLE
            buttonCheckout.visibility = View.VISIBLE
        }
    }

    private fun hideOtherViews() {
        binding.apply {
            rvCart.visibility = View.GONE
            totalBoxContainer.visibility = View.GONE
            buttonCheckout.visibility = View.GONE
        }
    }

    private fun hideEmptyCart() {
        binding.apply {
            layoutCartEmpty.visibility = View.GONE
        }
    }

    private fun showEmptyCart() {
        binding.apply {
            layoutCartEmpty.visibility = View.VISIBLE
        }
    }

    private fun setupCartRv() {
        binding.rvCart.apply{
            layoutManager = LinearLayoutManager( requireContext(), RecyclerView.VERTICAL, false )
            adapter = cartProductAdapter
            addItemDecoration(VerticalItemDecoration())
        }
    }

}
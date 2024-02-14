package com.psi.shoppingapp.fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.psi.shoppingapp.R
import com.psi.shoppingapp.adapters.AddressAdapter
import com.psi.shoppingapp.adapters.BillingProductAdapter
import com.psi.shoppingapp.data.CartProduct
import com.psi.shoppingapp.databinding.FragmentBillingBinding
import com.psi.shoppingapp.utils.HorizontalItemDecoration
import com.psi.shoppingapp.utils.Resource
import com.psi.shoppingapp.viewmodels.BillingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class BillingFragment : Fragment() {

    private lateinit var binding: FragmentBillingBinding
    private val addressAdapter by lazy { AddressAdapter() }
    private val billingProductAdapter by lazy { BillingProductAdapter() }
    private val viewModel by viewModels<BillingViewModel>()


    private val args by navArgs<BillingFragmentArgs>()
    private var cartProducts = emptyList<CartProduct>()
    private var totalPrice = 0f


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBillingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        totalPrice = args.totalPrice
        cartProducts = args.cartProducts.toList()


        setupBillingProductsRv()
        setupAddressRv()


        binding.imageAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }

        binding.imageCloseBilling.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonPlaceOrder.setOnClickListener {

        }


        lifecycleScope.launchWhenStarted {
            viewModel.addressList.collectLatest {
                when( it ) {
                    is Resource.Loading -> {
                        binding.progressbarAddress.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        addressAdapter.differ.submitList( it.data )
                        binding.progressbarAddress.visibility = View.GONE
                    }
                    is Resource.Error -> {
                        binding.progressbarAddress.visibility = View.GONE
                        Snackbar.make( requireView(), it.message.toString(), Snackbar.LENGTH_LONG )
                    }
                    else -> Unit
                }
            }

        }

        billingProductAdapter.differ.submitList(cartProducts)
        binding.tvTotalPrice.text = "$ ${String.format("%.2f", totalPrice)}"

        lifecycleScope.launchWhenStarted {
            viewModel.addressList.collectLatest {
                when( it ) {
                    is Resource.Loading -> {
                        binding.progressbarAddress.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressbarAddress.visibility = View.GONE
                    }
                    is Resource.Error -> {
                        Snackbar.make( requireView(), it.message.toString(), Snackbar.LENGTH_LONG )
                        binding.progressbarAddress.visibility = View.GONE
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setupAddressRv() {
        binding.rvAddress.apply {
            layoutManager = LinearLayoutManager( requireContext(), LinearLayoutManager.HORIZONTAL, false )
            adapter = addressAdapter
            addItemDecoration(HorizontalItemDecoration())
        }
    }

    private fun setupBillingProductsRv() {
        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager( requireContext(), LinearLayoutManager.HORIZONTAL, false )
            adapter = billingProductAdapter
            addItemDecoration(HorizontalItemDecoration())
        }
    }

}
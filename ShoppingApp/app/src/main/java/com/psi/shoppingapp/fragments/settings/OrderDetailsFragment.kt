package com.psi.shoppingapp.fragments.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.psi.shoppingapp.R
import com.psi.shoppingapp.adapters.BillingProductAdapter
import com.psi.shoppingapp.data.order.OrderStatus
import com.psi.shoppingapp.data.order.getOrderStatus
import com.psi.shoppingapp.databinding.FragmentOrderDetailsBinding
import com.psi.shoppingapp.utils.VerticalItemDecoration

class OrderDetailsFragment : Fragment() {

    private lateinit var binding: FragmentOrderDetailsBinding
    private val billingProductAdapter by lazy { BillingProductAdapter() }
    private val args by navArgs<OrderDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val order = args.order

        setupOrderRv()

        binding.apply {
            tvOrderId.text = "Order #${order.orderId}"
            stepView.setSteps(
                mutableListOf(
                    OrderStatus.Ordered.status,
                    OrderStatus.Confirmed.status,
                    OrderStatus.Shipped.status,
                    OrderStatus.Delivered.status
                )
            )

            var currentOrderStatus = when(getOrderStatus(order.orderStatus)) {
                is OrderStatus.Ordered -> 0
                is OrderStatus.Confirmed -> 1
                is OrderStatus.Shipped -> 2
                is OrderStatus.Delivered -> 3
                else -> 0
            }

            stepView.go( currentOrderStatus, false )
            if( currentOrderStatus == 3 ) {
                stepView.done(true)
            }

            tvAddress.text = "${order.address.street} ${order.address.city}"
            tvFullName.text = order.address.fullName
            tvPhoneNumber.text = order.address.phone

            tvTotalPrice.text = "$ ${order.totalPrice.toString()}"

            billingProductAdapter.differ.submitList(order.products)
        }

        binding.imageCloseOrder.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupOrderRv() {
        binding.rvProducts.apply {
            adapter = billingProductAdapter
            layoutManager = LinearLayoutManager( requireContext(), LinearLayoutManager.VERTICAL, false )
            addItemDecoration( VerticalItemDecoration() )
        }
    }

}
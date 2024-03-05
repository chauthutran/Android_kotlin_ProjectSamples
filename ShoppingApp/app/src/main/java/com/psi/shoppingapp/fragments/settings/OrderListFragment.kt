package com.psi.shoppingapp.fragments.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.psi.shoppingapp.adapters.OrderListAdapter
import com.psi.shoppingapp.databinding.FragmentOrderListBinding
import com.psi.shoppingapp.utils.Resource
import com.psi.shoppingapp.viewmodels.OrderListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class OrderListFragment : Fragment() {

    private lateinit var binding: FragmentOrderListBinding

    val viewModel by viewModels<OrderListViewModel>()
    val orderListAdapter by lazy { OrderListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvAllOrders.apply {
            adapter = orderListAdapter
            layoutManager = LinearLayoutManager( requireContext(), RecyclerView.VERTICAL, false )
        }

        binding.imageCloseOrders.setOnClickListener {
            findNavController().navigateUp()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.allOrders.collectLatest {
                when( it ) {
                    is Resource.Loading -> {
                        binding.progressbarAllOrders.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressbarAllOrders.visibility = View.GONE
                        orderListAdapter.differ.submitList(it.data)

                        if (it.data.isNullOrEmpty()) {
                            binding.tvEmptyOrders.visibility = View.VISIBLE
                        }
                    }
                    is Resource.Error -> {
                        binding.progressbarAllOrders.visibility = View.GONE
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        orderListAdapter.onClick = {
            val action = OrderListFragmentDirections.actionOrderListFragmentToOrderDetailsFragment(it)
            findNavController().navigate(action)
        }
    }

}
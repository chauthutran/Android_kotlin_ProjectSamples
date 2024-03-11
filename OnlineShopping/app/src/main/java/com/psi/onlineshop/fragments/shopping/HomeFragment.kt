package com.psi.onlineshop.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.psi.onlineshop.R
import com.psi.onlineshop.adapters.TodayProposalsAdapter
import com.psi.onlineshop.databinding.FragmentHomeBinding
import com.psi.onlineshop.utils.Resource
import com.psi.onlineshop.viewmodels.LoginViewModel
import com.psi.onlineshop.viewmodels.shopping.HomeViewModel
import kotlinx.coroutines.flow.collectLatest

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var todayProposalsAdapter: TodayProposalsAdapter

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.todayProposals.collectLatest {
                when(it) {
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        todayProposalsAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        setTodayProposalsAdapter()

        todayProposalsAdapter.onClick = {
//            var b = Bundle().apply { putParcelable("product", it) }
//            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment, b)

        }
    }

    private fun setTodayProposalsAdapter() {
        todayProposalsAdapter = TodayProposalsAdapter()
        binding.rvTodayProposals.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = todayProposalsAdapter
        }
    }

    private fun hideLoading() {
//        binding.mainCategoryProgressbar.visibility = View.GONE
    }

    private fun showLoading() {
//        binding.mainCategoryProgressbar.visibility = View.VISIBLE
    }

}
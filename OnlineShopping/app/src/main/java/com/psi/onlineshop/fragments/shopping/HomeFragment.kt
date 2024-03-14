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
import androidx.recyclerview.widget.GridLayoutManager
import com.psi.onlineshop.R
import com.psi.onlineshop.adapters.SliderImageAdapter
import com.psi.onlineshop.adapters.TodayProposalsAdapter
import com.psi.onlineshop.databinding.FragmentHomeBinding
import com.psi.onlineshop.utils.Resource
import com.psi.onlineshop.utils.SpacesItemDecoration
import com.psi.onlineshop.viewmodels.shopping.HomeViewModel
import com.smarteist.autoimageslider.SliderView
import kotlinx.coroutines.flow.collectLatest


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val todayProposalsAdapter: TodayProposalsAdapter by lazy { TodayProposalsAdapter() }
    private lateinit var sliderAdapter: SliderImageAdapter

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var sliderView: SliderView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ------------------ Create slider view ---------------------------------------------------
        //Initialize our slier view.
        sliderView = binding.sliderView
        var imageUrl = ArrayList<String>()
        // on below line we are adding data to our image url array list.
        imageUrl =
            (imageUrl + "https://practice.geeksforgeeks.org/_next/image?url=https%3A%2F%2Fmedia.geeksforgeeks.org%2Fimg-practice%2Fbanner%2Fdsa-self-paced-thumbnail.png&w=1920&q=75") as ArrayList<String>
        imageUrl =
            (imageUrl + "https://practice.geeksforgeeks.org/_next/image?url=https%3A%2F%2Fmedia.geeksforgeeks.org%2Fimg-practice%2Fbanner%2Fdata-science-live-thumbnail.png&w=1920&q=75") as ArrayList<String>
        imageUrl =
            (imageUrl + "https://practice.geeksforgeeks.org/_next/image?url=https%3A%2F%2Fmedia.geeksforgeeks.org%2Fimg-practice%2Fbanner%2Ffull-stack-node-thumbnail.png&w=1920&q=75") as ArrayList<String>


        // slider adapter and adding our list to it.
        sliderAdapter = SliderImageAdapter(imageUrl)
        // Set auto cycle direction for our slider view from left to right.
        sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
        // on below line we are setting auto cycle direction
        // for our slider view from left to right.
        sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR

        // on below line we are setting adapter for our slider.
        sliderView.setSliderAdapter(sliderAdapter)

        // on below line we are setting scroll time
        // in seconds for our slider view.
        sliderView.scrollTimeInSec = 3

        // on below line we are setting auto cycle
        // to true to auto slide our items.
        sliderView.isAutoCycle = true

        // on below line we are calling start
        // auto cycle to start our cycle.
        sliderView.startAutoCycle()

        // -----------------------------------------------------------------------------------------

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
            var b = Bundle().apply { putSerializable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment, b)
        }
    }

    private fun setTodayProposalsAdapter() {

        binding.rvTodayProposals.addItemDecoration( SpacesItemDecoration(60));

        binding.rvTodayProposals.apply {
            adapter = todayProposalsAdapter
            layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.HORIZONTAL, false)
        }
    }

    private fun hideLoading() {
//        binding.mainCategoryProgressbar.visibility = View.GONE
    }

    private fun showLoading() {
//        binding.mainCategoryProgressbar.visibility = View.VISIBLE
    }

}
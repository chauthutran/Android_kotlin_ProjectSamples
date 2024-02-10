package com.psi.shoppingapp.fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.psi.shoppingapp.R
import com.psi.shoppingapp.adapters.HomeViewPagerAdapter
import com.psi.shoppingapp.databinding.FragmentHomeBinding
import com.psi.shoppingapp.fragments.categories.AccessoryFragment
import com.psi.shoppingapp.fragments.categories.ChairFragment
import com.psi.shoppingapp.fragments.categories.FurnitureFragment
import com.psi.shoppingapp.fragments.categories.MainCategoryFragment

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragment = arrayListOf<Fragment>(
            MainCategoryFragment(),
            ChairFragment(),
            FurnitureFragment(),
            AccessoryFragment()
        )

        val viewPager2Adapter = HomeViewPagerAdapter(categoriesFragment, childFragmentManager, lifecycle )
        binding.viewpagerHome.adapter = viewPager2Adapter
        TabLayoutMediator(binding.tabLayout, binding.viewpagerHome){ tab, position ->
            when(position){
                0 -> tab.text = "Main"
                1 -> tab.text = "Chair"
                2 -> tab.text ="Furniture"
                3 -> tab.text = "Accessory"
            }
        }.attach()
    }
}
package com.psi.shoppingapp.fragments.loginregister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.psi.shoppingapp.R
import com.psi.shoppingapp.activities.ShoppingActivity
import com.psi.shoppingapp.databinding.FragmentIntroductionBinding
import com.psi.shoppingapp.viewmodels.IntroductionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IntroductionFragment : Fragment() {

    private lateinit var binding: FragmentIntroductionBinding
    private val viewModel by viewModels<IntroductionViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntroductionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.navigate.collect{
                when(it) {
                    IntroductionViewModel.SHOPPING_ACTIVITY -> {
                        Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent) // Click Back button of Android phone to come back the Login page
                        }
                    }

                    IntroductionViewModel.ACCOUNT_OPTIONS_FRAGMENT -> {
                        findNavController().navigate(it)
                    }

                    else -> Unit
                }
            }
        }

        binding.apply {
            viewModel.startButtonClick()
            buttonStart.setOnClickListener {
               findNavController().navigate(R.id.action_introductionFragment_to_accountOptionsFragment)
            }
        }
    }

}
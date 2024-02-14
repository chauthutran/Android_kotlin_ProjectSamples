package com.psi.shoppingapp.fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.psi.shoppingapp.R
import com.psi.shoppingapp.data.Address
import com.psi.shoppingapp.databinding.FragmentAddressBinding
import com.psi.shoppingapp.utils.Resource
import com.psi.shoppingapp.viewmodels.AddressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AddressFragment : Fragment() {

    private lateinit var binding: FragmentAddressBinding
    private val viewModel by viewModels<AddressViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddressBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
                viewModel.addNewAddressStatus.collectLatest {
                    when( it ) {
                        is Resource.Loading -> {
                            binding.progressbarAddress.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            binding.progressbarAddress.visibility = View.GONE
                            findNavController().navigateUp()
                        }
                        is Resource.Error -> {
                            Snackbar.make( requireView(), it.message.toString(), Snackbar.LENGTH_SHORT ).show()
                            binding.progressbarAddress.visibility = View.GONE
                        }
                        else -> Unit
                    }
                }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.error.collectLatest {
                Snackbar.make( requireView(), it, Snackbar.LENGTH_SHORT ).show()
            }
        }

        binding.apply {
            buttonSave.setOnClickListener {
                val addressTitle = edAddressTitle.text.toString()
                val fullName = edFullName.text.toString()
                val street = edStreet.text.toString()
                val phone = edPhone.text.toString()
                val city = edCity.text.toString()
                val state = edState.text.toString()

                val address = Address( addressTitle, fullName, street, phone, city, state )

                viewModel.addAddress( address )
            }

            imageAddressClose.setOnClickListener {
                findNavController().navigateUp()
            }
        }


    }
}
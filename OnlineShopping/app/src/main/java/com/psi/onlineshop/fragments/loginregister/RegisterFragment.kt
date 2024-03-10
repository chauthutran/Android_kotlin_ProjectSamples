package com.psi.onlineshop.fragments.loginregister

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.psi.onlineshop.R
import com.psi.onlineshop.data.User
import com.psi.onlineshop.databinding.FragmentRegisterBinding
import com.psi.onlineshop.utils.Resource
import com.psi.onlineshop.utils.UserRegisterValidation
import com.psi.onlineshop.viewmodels.RegisterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            buttonRegisterRegister.setOnClickListener{
                val user = User(
                    null,
                    editFirstNameRegister.text.toString().trim(),
                    editLastNameRegister.text.toString().trim(),
                    editEmailRegister.text.toString().trim(),
                    editPasswordRegister.text.toString()
                )

               viewModel.createUserAcccount(user)
            }

            tvLoginQuestion.setOnClickListener{
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.register.collect{
                when(it){
                    is Resource.Loading -> {
                        binding.buttonRegisterRegister.startAnimation()
                    }
                    is Resource.Success -> {
                        Toast.makeText(requireContext(), "Register successfully", Toast.LENGTH_SHORT).show()
                        binding.buttonRegisterRegister.revertAnimation()
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                        binding.buttonRegisterRegister.revertAnimation()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect {
                if( it.email is UserRegisterValidation.Failed ) {
                    withContext(Dispatchers.Main) {
                        binding.editEmailRegister.apply {
                            requestFocus()
                            error = it.email.message
                        }
                    }
                }

                if( it.password is UserRegisterValidation.Failed ) {
                    withContext(Dispatchers.Main) {
                        binding.editPasswordRegister.apply {
                            requestFocus()
                            error = it.password.message
                        }
                    }
                }

            }
        }


    }

}
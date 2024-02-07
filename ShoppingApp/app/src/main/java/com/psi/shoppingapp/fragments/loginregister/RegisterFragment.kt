package com.psi.shoppingapp.fragments.loginregister

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.psi.shoppingapp.data.User
import com.psi.shoppingapp.databinding.FragmentRegisterBinding
import com.psi.shoppingapp.utils.RegisterValidation
import com.psi.shoppingapp.utils.Resource
import com.psi.shoppingapp.viewmodels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

private val TAG = "RegisterFragment"

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
                    editFirstNameRegister.text.toString().trim(),
                    editLastNameRegister.text.toString().trim(),
                    editEmailRegister.text.toString().trim()
                )
                val password = editPasswordRegister.text.toString()
                viewModel.createAcccountWithEmailAndPassword(user, password)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.register.collect{
                when(it){
                    is Resource.Loading -> {
                        binding.buttonRegisterRegister.startAnimation()
                    }
                    is Resource.Success -> {
                        Log.d("test", it.message.toString())
                        binding.buttonRegisterRegister.revertAnimation()
                    }
                    is Resource.Error -> {
                        Log.d(TAG, it.message.toString())
                        binding.buttonRegisterRegister.revertAnimation()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect {
                if( it.email is RegisterValidation.Failed ) {
                    withContext(Dispatchers.Main) {
                        binding.editEmailRegister.apply {
                            requestFocus()
                            error = it.email.message
                        }
                    }
                }

                if( it.password is RegisterValidation.Failed ) {
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
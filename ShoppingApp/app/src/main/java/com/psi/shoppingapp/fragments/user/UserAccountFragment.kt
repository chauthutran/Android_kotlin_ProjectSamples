package com.psi.shoppingapp.fragments.user

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.psi.shoppingapp.data.User
import com.psi.shoppingapp.databinding.FragmentUserAccountBinding
import com.psi.shoppingapp.utils.Resource
import com.psi.shoppingapp.viewmodels.UserAccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class UserAccountFragment : Fragment() {

    private lateinit var binding: FragmentUserAccountBinding
    private val viewModel by viewModels<UserAccountViewModel>()
    private lateinit var imageActivityResultLauncher: ActivityResultLauncher<Intent>

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                imageUri = it.data?.data
                Glide.with(this).load(imageUri).into(binding.imageUser)
            }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserAccountBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageCloseUserAccount.setOnClickListener {
            findNavController().navigateUp()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.user.collectLatest {
                when( it ) {
                    is Resource.Loading -> {
                        showUserLoading()
                    }
                    is Resource.Success -> {
                        hideUserLoading()
                        showUserInformation(it.data!!)
                    }
                    is Resource.Error -> {
                        Snackbar.make( requireView(), it.message.toString(), Snackbar.LENGTH_SHORT ).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.updateInfo.collectLatest {
                when( it ) {
                    is Resource.Loading -> {
                        binding.buttonSave.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.buttonSave.revertAnimation()
                        Snackbar.make( requireView(), "User information is saved.", Snackbar.LENGTH_SHORT ).show()
                        findNavController().navigateUp()
                    }
                    is Resource.Error -> {
                        binding.buttonSave.revertAnimation()
                        Snackbar.make( requireView(), it.message.toString(), Snackbar.LENGTH_SHORT ).show()
                    }
                    else -> Unit
                }
            }
        }

        binding.buttonSave.setOnClickListener {
            print("=================== binding.buttonSave.setOnClickListener")
            binding.apply {
                val firstName = edFirstName.text.toString().trim()
                val lastName = edLastName.text.toString().trim()
                val email = edEmail.text.toString().trim()

                val user = User(firstName, lastName, email)
                viewModel.updateUser(user, imageUri)
            }
        }

        binding.imageEdit.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            imageActivityResultLauncher.launch(intent)
        }
    }

    private fun showUserInformation(user: User) {
        binding.apply {
            Glide.with(this@UserAccountFragment).load(user.imagePath).error(ColorDrawable(Color.BLACK)).into(imageUser)
            edFirstName.setText(user.firstName)
            edLastName.setText(user.lastName)
            edEmail.setText(user.email)
        }
    }

    private fun showUserLoading() {
        binding.apply {
            progressbarAccount.visibility = View.VISIBLE
        }
    }

    private fun hideUserLoading() {
        binding.apply {
            progressbarAccount.visibility = View.GONE
        }
    }
}
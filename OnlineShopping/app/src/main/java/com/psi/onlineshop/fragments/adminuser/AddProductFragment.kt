package com.psi.onlineshop.fragments.adminuser

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.psi.onlineshop.R
import com.psi.onlineshop.activities.ShoppingActivity
import com.psi.onlineshop.data.Product
import com.psi.onlineshop.data.ProductVariant
import com.psi.onlineshop.databinding.FragmentAddProductBinding
import com.psi.onlineshop.utils.Resource
import com.psi.onlineshop.utils.getCurrentDateStr
import com.psi.onlineshop.viewmodels.AddProductViewModel
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import java.io.ByteArrayOutputStream
import java.util.UUID

class AddProductFragment : Fragment() {

    private lateinit var binding: FragmentAddProductBinding
    private val viewModel by viewModels<AddProductViewModel>()

    var selectedImages = mutableMapOf<String,ByteArray>()
//    var productVariant = ProductVariant()
    var productVariantList = ArrayList<ProductVariant>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProductBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpButtonColorPicker()
        setUpButtonImagePicker()
        setUpButtonAddDetails()
        setUpButtonSaveProduct()


        lifecycleScope.launchWhenStarted {
            viewModel.addNewProduct.collect { it ->
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbar.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressbar.visibility = View.GONE
                        productVariantList = ArrayList<ProductVariant>()
                    }

                    is Resource.Error -> {
                        binding.progressbar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }



    }

    private fun setUpButtonColorPicker() {
        binding.buttonColorPicker.setOnClickListener {
            ColorPickerDialog
                .Builder(requireContext())
                .setTitle("Product color")
                .setPositiveButton("Select", object : ColorEnvelopeListener {

                    override fun onColorSelected(envelope: ColorEnvelope?, fromUser: Boolean) {
                        envelope?.let {

//                            productVariant.color = it
                            val colorDrawable = ColorDrawable(it.color)
                            binding.edSelectedColor.background = colorDrawable
                            binding.edSelectedColor.setText( it.color.toString() )
                        }
                    }

                }).setNegativeButton("Cancel") { colorPicker, _ ->
                    colorPicker.dismiss()
                }.show()
        }
    }

    private fun setUpButtonImagePicker() {
        val selectImagesActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val intent = result.data

                    var imgName = "img-${UUID.randomUUID()}.jpg"

                    //Multiple images selected
                    if (intent?.clipData != null) {
                        val count = intent.clipData?.itemCount ?: 1
                            val imagesUri = intent.clipData?.getItemAt(count - 1)?.uri
                            imagesUri?.let {
                                val inputStream = requireContext().contentResolver.openInputStream(imagesUri)
                                inputStream?.buffered()?.use {
                                    var imgByteArr = it.readBytes()

                                    selectedImages[imgName] = imgByteArr
                                    binding.tvSelectedImage.text = imgName
                                }
                        }
                    }
                    else {  //One images was selected
                        val imageUri = intent?.data
                        imageUri?.let {
                            val inputStream = requireContext().contentResolver.openInputStream(imageUri)
                            inputStream?.buffered()?.use {
                                var imgByteArr = it.readBytes()

                                selectedImages[imgName] = imgByteArr
                                binding.tvSelectedImage.text = imgName
                            }
                        }

                    }

                    // Update image selected
                    binding.tvSelectedImage.text = imgName
                }
            }

        binding.buttonImagesPicker.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
            intent.type = "image/*"
            selectImagesActivityResult.launch(intent)
        }
    }

    private fun setUpButtonAddDetails() {
        binding.buttonAddDetails.setOnClickListener {

            val size = binding.edSize.text.toString().trim()
            val stockQuantity = binding.edQuantity.text.toString().trim().toInt()
            val price = binding.edPrice.text.toString().trim().toFloat()
            val color = binding.edSelectedColor.text.toString().toInt()
            var imageName = binding.tvSelectedImage.text.toString()
            var offerPercentage: Float? = null
println(" -========= color : $color ")
            binding.edOfferPercentage.text?.let {
                offerPercentage = binding.edOfferPercentage.text.toString().trim().toFloat()
            }

            val productVariant = ProductVariant( price, offerPercentage, stockQuantity, imageName, color, size )

            productVariantList.add(productVariant)

            // Reset data
            binding.tvSelectedImage.text = ""
            val colorDrawable = ColorDrawable(resources.getColor(R.color.white))
            binding.edSelectedColor.background = colorDrawable
        }
    }

    private fun setUpButtonSaveProduct() {
        binding.buttonSaveProduct.setOnClickListener {
            val name = binding.edName.text.toString().trim()
            val category = binding.edCategory.text.toString().trim()
            val productDescription = binding.edDescription.text.toString().trim()

            val product = Product(
                UUID.randomUUID().toString(),
                name,
                if (productDescription.isEmpty()) null else productDescription,
                category,
                productVariantList
            )
            viewModel.saveProduct( product, selectedImages  )
        }
    }
}
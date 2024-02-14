package com.psi.shoppingapp.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.psi.shoppingapp.R
import com.psi.shoppingapp.data.Address
import com.psi.shoppingapp.data.CartProduct
import com.psi.shoppingapp.databinding.BillingProductsRvItemBinding
import com.psi.shoppingapp.helper.getProductPrice

class BillingProductAdapter: RecyclerView.Adapter<BillingProductAdapter.BillingProductViewHolder>() {

    inner class BillingProductViewHolder(val binding: BillingProductsRvItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind( billingProduct: CartProduct ) {
            binding.apply {

                if( !billingProduct.product.images.isNullOrEmpty() ) {
                    Glide.with(itemView).load(billingProduct.product.images[0]).into(imageCartProduct)
                }

                tvProductCartName.text = billingProduct.product.name
                tvBillingProductQuantity.text = billingProduct.quantity.toString()
                tvCartProductSize.text = billingProduct.selectedSize
                imageCartProductColor.setImageDrawable(ColorDrawable(billingProduct.selectedColor?: Color.TRANSPARENT))

                val priceAfterPercentage = billingProduct.product.offerPercentage.getProductPrice(billingProduct.product.price)
                tvProductCartPrice.text = "$  ${String.format("%.2f", priceAfterPercentage )}"
            }
        }
    }

    private val diffCallBack = object: DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product == newItem.product
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingProductAdapter.BillingProductViewHolder {
        return BillingProductViewHolder(
            BillingProductsRvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BillingProductViewHolder, position: Int) {
        val buildingProduct = differ.currentList[position]
        holder.bind(buildingProduct)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}
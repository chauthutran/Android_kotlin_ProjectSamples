package com.psi.onlineshop.adapters

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.psi.onlineshop.R
import com.psi.onlineshop.data.Product
import com.psi.onlineshop.data.ProductVariant
import com.psi.onlineshop.databinding.ProductColorItemBinding
import com.psi.onlineshop.httpRequest.HttpRequestConfig

class ProductColorsAdapter: RecyclerView.Adapter<ProductColorsAdapter.ColorsViewHolder>() {

    private var selectedPosition = -1
    inner class ColorsViewHolder(private val binding: ProductColorItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(productVariant: ProductVariant, position: Int) {
            // For image
            productVariant.imageName?.let {
                val imageUrl = "${HttpRequestConfig.BASE_URL_MONGODB_SERVICE}/file/${productVariant.imageName}"
                Glide.with(itemView).load(imageUrl).into(binding.imageProduct)
            }

            // For TextView
            productVariant.color?.let {
                val imageDrawable = ColorDrawable(productVariant.color!!)
                binding.tvColor.background = imageDrawable
            }

            // For Selected/Un-selected item
            val background: GradientDrawable = itemView.background as GradientDrawable
            if( position == selectedPosition ) // The color is selected
            {
                val colorBlue = itemView.resources.getColor(R.color.l_blue)
                background.setStroke(6, colorBlue)
            }
            else // The color is NOT selected
            {
                val colorBlack = itemView.resources.getColor(R.color.black)
                background.setStroke(4, colorBlack)
            }
        }

    }

    private val diffCallBack = object: DiffUtil.ItemCallback<ProductVariant>() {
        override fun areContentsTheSame(oldItem: ProductVariant, newItem: ProductVariant): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: ProductVariant, newItem: ProductVariant): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer( this, diffCallBack )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorsViewHolder {
        return ColorsViewHolder(
            ProductColorItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }
    override fun onBindViewHolder(holder: ColorsViewHolder, position: Int) {
        var productVariant = differ.currentList[position]
        holder.bind(productVariant, position)

        holder.itemView.setOnClickListener {
            if( selectedPosition >= 0 )
                notifyItemChanged(selectedPosition)

            selectedPosition = holder.adapterPosition
            notifyItemChanged( selectedPosition )
            onItemClick?.invoke(productVariant)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onItemClick: ((ProductVariant) -> Unit ) ?= null
}
package com.psi.shoppingapp.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.psi.shoppingapp.data.Product
import com.psi.shoppingapp.databinding.ProductRvItemBinding

class BestProductsAdapter: RecyclerView.Adapter<BestProductsAdapter.BestProductsViewHolder>() {
    inner class BestProductsViewHolder( private val binding: ProductRvItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                if(product.images.isNotEmpty())
                {
                    Glide.with(itemView).load(product.images[0]).into(imgProduct)
                }

                tvName.text = product.name
                tvPrice.text = "$ ${product.price.toString()}"

                if( product.offerPercentage == null ) {
                    tvNewPrice.visibility = View.INVISIBLE
                }
                else {
                    val newPrice = product.price * ( 1f - product.offerPercentage )

                    println(String.format("%.2f", newPrice))
                    tvNewPrice.text = "$ ${String.format("%.2f", newPrice)}"

                    tvPrice.paintFlags = tvPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
            }
        }
    }


    val diffCallBack = object: DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this, diffCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestProductsAdapter.BestProductsViewHolder {
        return BestProductsViewHolder(
            ProductRvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BestProductsAdapter.BestProductsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    var onClick:((Product) -> Unit)? = null
}
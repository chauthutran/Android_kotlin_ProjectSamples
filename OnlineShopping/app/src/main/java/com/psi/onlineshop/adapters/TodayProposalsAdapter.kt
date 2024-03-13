package com.psi.onlineshop.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.psi.onlineshop.data.Product
import com.psi.onlineshop.databinding.RvProductItemBinding

class TodayProposalsAdapter : RecyclerView.Adapter<TodayProposalsAdapter.TodayProposalsViewHolder>() {
    inner class TodayProposalsViewHolder(private val binding: RvProductItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                println( "=======: ${product.images.isNotEmpty()}")
                if(product.images.isNotEmpty())
                {
                    Glide.with(itemView).load(product.images[0]).into(imageSpecialRvItem)
                }

                tvSpecialProductName.text = product.name
                tvSpecialPrdouctPrice.text = product.price.toString()
            }
        }
    }

    val diffCallBack = object: DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayProposalsViewHolder {
        return TodayProposalsViewHolder(
            RvProductItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: TodayProposalsViewHolder, position: Int) {
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
package com.psi.onlineshop.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.psi.onlineshop.data.Product
import com.psi.onlineshop.databinding.RvProductItemBinding
import com.psi.onlineshop.httpRequest.HttpRequestConfig
import com.psi.onlineshop.httpRequest.HttpRequestUtil

class TodayProposalsAdapter : RecyclerView.Adapter<TodayProposalsAdapter.TodayProposalsViewHolder>() {
    inner class TodayProposalsViewHolder(private val binding: RvProductItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                if(product.imgFileIds.isNotEmpty())
                {
                    var url = "${HttpRequestConfig.BASE_URL_MONGODB_SERVICE}/file/${product.imgFileIds[0]}"
                    Glide.with(itemView).load(url).into(imageProduct)
                }

                tvProductName.text = product.name


                if( product.offerPercentage == null ) {
                    tvProductOriginalPrice.visibility = View.GONE
                    tvOfferPercentage.visibility = View.GONE
                    tvProductRealPrice.text = "$ ${String.format("%.2f", product.price)}"
                }
                else {
                    tvProductOriginalPrice.text = "$ ${String.format("%.2f", product.price)}"
                    tvOfferPercentage.text = "${String.format("%.2f", product.offerPercentage * 100)}%"
                    tvProductOriginalPrice.paintFlags = tvProductOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                    val newPrice = product.price * (1f - product.offerPercentage)
                    tvProductRealPrice.text = "$ ${String.format("%.2f", newPrice)}"
                }


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
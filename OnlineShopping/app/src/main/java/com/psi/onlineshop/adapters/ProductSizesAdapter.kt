package com.psi.onlineshop.adapters

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.psi.onlineshop.R
import com.psi.onlineshop.databinding.ProductSizeItemBinding


class ProductSizesAdapter( ) : RecyclerView.Adapter<ProductSizesAdapter.SizesViewHolder>() {

    private var selectedPosition = -1

    inner class SizesViewHolder(private val binding: ProductSizeItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(size: String, position: Int) {
println("------------ bind size ")
            binding.tvSize.text = size
            val background: GradientDrawable = binding.tvSize.background as GradientDrawable

            var resources = itemView.resources
            // The Size is selected
            if (position == selectedPosition) {
                val colorBlue = resources.getColor(R.color.l_blue)
                background.setStroke(6, colorBlue)
            }
            // The Size is NOT selected
            else {
                val colorBlack = resources.getColor(R.color.black)
                background.setStroke(4, colorBlack)
            }
        }
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<String>() {
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizesViewHolder {
        return SizesViewHolder(
            ProductSizeItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: SizesViewHolder, position: Int) {
        var size = differ.currentList[position]
        holder.bind(size, position)

        holder.itemView.setOnClickListener {
            if (selectedPosition >= 0)
                notifyItemChanged(selectedPosition)

            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onItemClick?.invoke(size)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    var onItemClick: ((String) -> Unit)? = null

}
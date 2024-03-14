package com.psi.onlineshop.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.psi.onlineshop.databinding.ProductColorItemBinding

class ProductColorsAdapter: RecyclerView.Adapter<ProductColorsAdapter.ColorsViewHolder>() {

    private var selectedPosition = -1
    inner class ColorsViewHolder(private val binding: ProductColorItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(color: Int, position: Int) {
            val imageDrawable = ColorDrawable(color)
            binding.imageColor.setImageDrawable(imageDrawable)

            if( position == selectedPosition ) // The color is selected
            {
                binding.apply {
                    imageShadow.visibility = View.VISIBLE
                    imagePicked.visibility = View.VISIBLE
                }
            }
            else // The color is NOT selected
            {
                binding.apply {
                    imageShadow.visibility = View.INVISIBLE
                    imagePicked.visibility = View.INVISIBLE
                }
            }
        }

    }

    private val diffCallBack = object: DiffUtil.ItemCallback<Int>() {
        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
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
        var color = differ.currentList[position]
        holder.bind(color, position)

        holder.itemView.setOnClickListener {
            if( selectedPosition >= 0 )
                notifyItemChanged(selectedPosition)

            selectedPosition = holder.adapterPosition
            notifyItemChanged( selectedPosition )
            onItemClick?.invoke(color)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onItemClick: ((Int) -> Unit ) ?= null
}
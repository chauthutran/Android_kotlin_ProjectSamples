package com.psi.shoppingapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.psi.shoppingapp.databinding.ViewPagerImagesItemBinding

class ViewPager2ImagesAdapter: RecyclerView.Adapter<ViewPager2ImagesAdapter.ViewPage2ImageViewHolder>() {

    inner class ViewPage2ImageViewHolder ( val binding: ViewPagerImagesItemBinding ): ViewHolder(binding.root   ) {
        fun bind( imagePath: String ) {
            Glide.with(itemView).load(imagePath).into(binding.imageProductDetails)
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this, diffCallback )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPage2ImageViewHolder {
        return ViewPage2ImageViewHolder(
            ViewPagerImagesItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewPage2ImageViewHolder, position: Int) {
        var image = differ.currentList[position]
        holder.bind(image)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}
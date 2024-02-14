package com.psi.shoppingapp.adapters

import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.psi.shoppingapp.R
import com.psi.shoppingapp.data.Address
import com.psi.shoppingapp.data.Product
import com.psi.shoppingapp.databinding.AddressRvItemBinding
import com.psi.shoppingapp.databinding.BestDealsRvItemBinding
import com.psi.shoppingapp.helper.getProductPrice

class AddressAdapter: RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {
    inner class AddressViewHolder(val binding: AddressRvItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(address: Address, isSelected: Boolean) {
            binding.apply {
                buttonAddress.text = address.addressTitle
                if( isSelected ){
                    buttonAddress.background = ColorDrawable(itemView.context.resources.getColor(R.color.g_blue))
                }
                else {
                    buttonAddress.background = ColorDrawable(itemView.context.resources.getColor(R.color.g_white))
                }
            }
        }
    }


    private val diffCallBack = object: DiffUtil.ItemCallback<Address>() {
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this, diffCallBack)

    private var selectedAddress = -1

    init {
        differ.addListListener { _,_ ->
            notifyItemChanged((selectedAddress))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressAdapter.AddressViewHolder {
        return AddressViewHolder(
            AddressRvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = differ.currentList[position]
        holder.bind(address, selectedAddress == position)

        holder.binding.buttonAddress.setOnClickListener {
            if( selectedAddress >= 0 ) {
                notifyItemChanged(selectedAddress)
            }

            selectedAddress = holder.adapterPosition
            notifyItemChanged( selectedAddress)
            onClick?.invoke(address)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    var onClick:((Address) -> Unit)? = null

}
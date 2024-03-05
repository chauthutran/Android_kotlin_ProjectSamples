package com.psi.shoppingapp.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.psi.shoppingapp.R
import com.psi.shoppingapp.data.Address
import com.psi.shoppingapp.data.order.Order
import com.psi.shoppingapp.data.order.OrderStatus
import com.psi.shoppingapp.data.order.getOrderStatus
import com.psi.shoppingapp.databinding.AddressRvItemBinding
import com.psi.shoppingapp.databinding.OrderItemBinding

class OrderListAdapter: RecyclerView.Adapter<OrderListAdapter.OrderViewHolder>() {

    inner class OrderViewHolder( private val binding: OrderItemBinding ): RecyclerView.ViewHolder(binding.root)
    {
        fun bind( order: Order )
        {
            binding.apply {
                tvOrderId.text = order.orderId.toString()
                tvOrderDate.text = order.date

                var resources = itemView.resources
                val colorDrawable = when ( getOrderStatus(order.orderStatus))
                {
                    is OrderStatus.Ordered -> {
                        ColorDrawable(resources.getColor(R.color.g_orange_yellow))
                    }
                    is OrderStatus.Confirmed -> {
                        ColorDrawable(resources.getColor(R.color.g_green))
                    }
                    is OrderStatus.Delivered -> {
                        ColorDrawable(resources.getColor(R.color.g_green))
                    }
                    is OrderStatus.Shipped -> {
                        ColorDrawable(resources.getColor(R.color.g_green))
                    }
                    is OrderStatus.Canceled -> {
                        ColorDrawable(resources.getColor(R.color.g_red))
                    }
                    is OrderStatus.Returned -> {
                        ColorDrawable(resources.getColor(R.color.g_red))
                    }
                }
                imageOrderState.setImageDrawable(colorDrawable)
            }
        }
    }

    private val diffUtil = object: DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.products == newItem.products
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListAdapter.OrderViewHolder {
        return OrderViewHolder(
            OrderItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: OrderListAdapter.OrderViewHolder, position: Int) {
        val order = differ.currentList[position]
        holder.bind(order)

        holder.itemView.setOnClickListener {
            onClick?.invoke(order)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick:((Order) -> Unit)? = null
}
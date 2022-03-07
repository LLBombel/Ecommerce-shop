package com.rafalropel.ecommerceshop.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rafalropel.ecommerceshop.OrderDetailsActivity
import com.rafalropel.ecommerceshop.databinding.CartItemBinding
import com.rafalropel.ecommerceshop.model.Order
import com.rafalropel.ecommerceshop.utils.Constants
import com.rafalropel.ecommerceshop.utils.GlideLoader

class OrdersAdapter(private val context: Context, private var list: ArrayList<Order>) :
    RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersAdapter.ViewHolder {
        return ViewHolder(
            CartItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OrdersAdapter.ViewHolder, position: Int) {
        val item = list[position]

        GlideLoader(context).loadProductPicture(item.image, holder.ivCartItemImage)

        holder.tvCartItemTitle.text = item.title
        holder.tvCartItemPrice.text = item.total_amount

        holder.ibDeleteCartItem.visibility = View.GONE
        holder.ibRemoveCartItem.visibility = View.GONE
        holder.tvCartQuantity.visibility = View.GONE
        holder.ibCartAddItem.visibility = View.GONE

        holder.itemView.setOnClickListener {
            val intent = Intent(context, OrderDetailsActivity::class.java)
            intent.putExtra(Constants.EXTRA_ORDER_DETAILS, item)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(binding: CartItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val ivCartItemImage = binding.ivCartItemImage
        val tvCartItemTitle = binding.tvCartItemTitle
        val tvCartItemPrice = binding.tvCartItemPrice
        val ibRemoveCartItem = binding.ibRemoveCartItem
        val tvCartQuantity = binding.tvCartQuantity
        val ibCartAddItem = binding.ibAddCartItem
        val ibDeleteCartItem = binding.ibDeleteCartItem
    }
}
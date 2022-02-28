package com.rafalropel.ecommerceshop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rafalropel.ecommerceshop.databinding.CartItemBinding
import com.rafalropel.ecommerceshop.model.Cart
import com.rafalropel.ecommerceshop.utils.GlideLoader

class CartItemsAdapter(private val context: Context, private val list: ArrayList<Cart>) :
    RecyclerView.Adapter<CartItemsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemsAdapter.ViewHolder {
        return ViewHolder(
            CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CartItemsAdapter.ViewHolder, position: Int) {
       val item = list[position]

        GlideLoader(context).loadProductPicture(item.image, holder.ivCartItemImage)
        holder.tvCartItemTitle.text = item.title
        holder.tvCartItemPrice.text = item.price
        holder.tvCartQuantity.text = item.cart_amount

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
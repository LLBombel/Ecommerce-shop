package com.rafalropel.ecommerceshop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rafalropel.ecommerceshop.R
import com.rafalropel.ecommerceshop.databinding.CartItemBinding
import com.rafalropel.ecommerceshop.firestore.FireStoreClass
import com.rafalropel.ecommerceshop.model.Cart
import com.rafalropel.ecommerceshop.utils.Constants
import com.rafalropel.ecommerceshop.utils.GlideLoader

class CartItemsAdapter(private val context: Context, private val list: ArrayList<Cart>) :
    RecyclerView.Adapter<CartItemsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        GlideLoader(context).loadProductPicture(item.image, holder.ivCartItemImage)
        holder.tvCartItemTitle.text = item.title
        holder.tvCartItemPrice.text = item.price
        holder.tvCartQuantity.text = item.cart_amount


        if (item.cart_amount == "0") {
            holder.ibRemoveCartItem.visibility = View.GONE
            holder.ibCartAddItem.visibility = View.GONE

            holder.tvCartQuantity.text = context.getString(R.string.out_of_stock)

            holder.tvCartQuantity.setTextColor(ContextCompat.getColor(context, R.color.ThemePink))
        } else {
            holder.ibRemoveCartItem.visibility = View.VISIBLE
            holder.ibCartAddItem.visibility = View.VISIBLE

            holder.tvCartQuantity.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.SecondaryText
                )
            )
        }

        holder.ibDeleteCartItem.setOnClickListener {

            FireStoreClass().removeItemFromCart(context, item.id)

        }

        holder.ibRemoveCartItem.setOnClickListener {
            if (item.cart_amount == "1") {
                FireStoreClass().removeItemFromCart(context, item.id)
            } else {
                val cartAmount: Int = item.cart_amount.toInt()

                val itemHashMap = HashMap<String, Any>()

                itemHashMap[Constants.CART_AMOUNT] = (cartAmount - 1).toString()

                FireStoreClass().updateCart(context, item.id, itemHashMap)
            }
        }

        holder.ibCartAddItem.setOnClickListener {
            val cartAmount: Int = item.cart_amount.toInt()

            if(cartAmount < item.amount.toInt()){
                val itemHashMap = HashMap<String, Any>()

                itemHashMap[Constants.CART_AMOUNT] = (cartAmount + 1).toString()
                FireStoreClass().updateCart(context, item.id, itemHashMap)
            }


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
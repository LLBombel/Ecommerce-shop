package com.rafalropel.ecommerceshop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rafalropel.ecommerceshop.databinding.ProductHomeItemBinding
import com.rafalropel.ecommerceshop.model.Product
import com.rafalropel.ecommerceshop.utils.GlideLoader

class ProductHomeAdapter(private val context: Context, private var list: ArrayList<Product>) :
    RecyclerView.Adapter<ProductHomeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductHomeAdapter.ViewHolder {
        return ViewHolder(
            ProductHomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductHomeAdapter.ViewHolder, position: Int) {
        val item = list[position]

        holder.tvHomeItemTitle.text = item.title
        holder.tvHomeItemPrice.text = item.price

        GlideLoader(context).loadProductPicture(item.image, holder.ivHomeItemImage)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(binding: ProductHomeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val ivHomeItemImage = binding.ivHomeItemImage
        val viewDivider = binding.viewDivider
        val llHomeItemDetails = binding.llHomeItemDetails
        val tvHomeItemTitle = binding.tvHomeItemTitle
        val tvHomeItemPrice = binding.tvHomeItemPrice
    }
}
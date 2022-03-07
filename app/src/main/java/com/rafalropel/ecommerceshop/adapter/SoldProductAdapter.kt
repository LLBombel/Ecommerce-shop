package com.rafalropel.ecommerceshop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rafalropel.ecommerceshop.databinding.ProductItemBinding
import com.rafalropel.ecommerceshop.model.SoldProduct
import com.rafalropel.ecommerceshop.utils.GlideLoader

class SoldProductAdapter(private val context: Context, private val list: ArrayList<SoldProduct>) :
    RecyclerView.Adapter<SoldProductAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SoldProductAdapter.ViewHolder {
        return ViewHolder(
            ProductItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SoldProductAdapter.ViewHolder, position: Int) {
        val item = list[position]

        GlideLoader(context).loadProductPicture(item.image, holder.ivItemImage)

        holder.tvItemName.text = item.title
        holder.tvItemPrice.text = item.price

        holder.btnDeleteProduct.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(binding: ProductItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var llItemImage = binding.llItemImage
        val tvItemName = binding.tvItemName
        val tvItemPrice = binding.tvItemPrice
        val ivItemImage = binding.ivItemImage
        val btnDeleteProduct = binding.btnDeleteProduct
    }
}
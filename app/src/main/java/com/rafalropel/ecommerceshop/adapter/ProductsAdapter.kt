package com.rafalropel.ecommerceshop.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rafalropel.ecommerceshop.ProductDetailsActivity
import com.rafalropel.ecommerceshop.databinding.ProductItemBinding
import com.rafalropel.ecommerceshop.model.Product
import com.rafalropel.ecommerceshop.ui.dashboard.ProductsFragment
import com.rafalropel.ecommerceshop.utils.Constants
import com.rafalropel.ecommerceshop.utils.GlideLoader

class ProductsAdapter(private val context: Context, private var list: ArrayList<Product>, private val fragment: ProductsFragment) :
    RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsAdapter.ViewHolder {
        return ViewHolder(
            ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductsAdapter.ViewHolder, position: Int) {
        val item = list[position]


        GlideLoader(context).loadProductPicture(item.image, holder.ivItemImage)
        holder.tvItemName.text = item.title
        holder.tvItemPrice.text = item.price
        holder.btnDeleteProduct.setOnClickListener {
            fragment.deleteProduct(item.product_id)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra(Constants.EXTRA_PRODUCT_ID, item.product_id)
            intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID, item.user_id)
            context.startActivity(intent)
        }

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

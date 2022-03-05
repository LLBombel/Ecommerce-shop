package com.rafalropel.ecommerceshop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rafalropel.ecommerceshop.databinding.AddressItemBinding
import com.rafalropel.ecommerceshop.model.Address

class AddressAdapter(private val context: Context, private var list: ArrayList<Address>) :
    RecyclerView.Adapter<AddressAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AddressItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.tvAddressFullName.text = item.name
        holder.tvAddressType.text = item.type
        holder.tvAddressMobileNumber.text = item.phoneNumber
        holder.tvAddressDetails.text = "${item.address}, ${item.zipCode}"
    }


    override fun getItemCount(): Int {
        return list.size
    }


    class ViewHolder(binding: AddressItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvAddressFullName = binding.tvAddressFullName
        val tvAddressDetails = binding.tvAddressDetails
        val tvAddressType = binding.tvAddressType
        val tvAddressMobileNumber = binding.tvAddressMobileNumber
    }
}
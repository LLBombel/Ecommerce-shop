package com.rafalropel.ecommerceshop.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.rafalropel.ecommerceshop.AddEditAddressActivity
import com.rafalropel.ecommerceshop.CheckoutActivity
import com.rafalropel.ecommerceshop.databinding.AddressItemBinding
import com.rafalropel.ecommerceshop.model.Address
import com.rafalropel.ecommerceshop.utils.Constants

class AddressAdapter(
    private val context: Context,
    private var list: ArrayList<Address>,
    private val selectAddress: Boolean
) :
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

    fun notifyEditItem(activity: Activity, position: Int) {
        val intent = Intent(context, AddEditAddressActivity::class.java)
        intent.putExtra(Constants.EXTRA_ADDRESS_DETAILS, list[position])
        activity.startActivityForResult(intent, Constants.ADD_ADDRESS_REQUEST_CODE)
        notifyItemChanged(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.tvAddressFullName.text = item.name
        holder.tvAddressType.text = item.type
        holder.tvAddressMobileNumber.text = item.phoneNumber
        holder.tvAddressDetails.text = "${item.address}, ${item.zipCode}"

        if (selectAddress) {
            holder.itemView.setOnClickListener {
                val intent = Intent(context, CheckoutActivity::class.java)
                intent.putExtra(Constants.EXTRA_SELECTED_ADDRESS, item)
                context.startActivity(intent)
            }
        }
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
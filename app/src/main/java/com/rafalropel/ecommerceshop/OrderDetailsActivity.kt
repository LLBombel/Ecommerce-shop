package com.rafalropel.ecommerceshop

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rafalropel.ecommerceshop.adapter.CartItemsAdapter
import com.rafalropel.ecommerceshop.databinding.ActivityOrderDetailsBinding
import com.rafalropel.ecommerceshop.model.Order
import com.rafalropel.ecommerceshop.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

private lateinit var binding: ActivityOrderDetailsBinding

class OrderDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()


        var orderDetails = Order()
        if (intent.hasExtra(Constants.EXTRA_ORDER_DETAILS)) {
            orderDetails = intent.getParcelableExtra(Constants.EXTRA_ORDER_DETAILS)!!
        }

        setupUI(orderDetails)
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarMyOrderDetailsActivity)

        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_back)
        }
        binding.toolbarMyOrderDetailsActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupUI(orderDetails: Order) {
        binding.tvOrderDetailsId.text = orderDetails.title
        val dateFormat = "dd MMM yyyy HH:mm"
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())

        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = orderDetails.order_date

        val orderDateTime = formatter.format(calendar.time)

        binding.tvOrderDetailsDate.text = orderDateTime

        binding.rvMyOrderItemsList.layoutManager = LinearLayoutManager(this)
        binding.rvMyOrderItemsList.setHasFixedSize(true)

        val cartListAdapter = CartItemsAdapter(this, orderDetails.items, false)
        binding.rvMyOrderItemsList.adapter = cartListAdapter

        binding.tvMyOrderDetailsAddressType.text = orderDetails.address.type
        binding.tvMyOrderDetailsFullName.text = orderDetails.address.name
        binding.tvMyOrderDetailsAddress.text =
            "${orderDetails.address.address}, ${orderDetails.address.zipCode}"
        binding.tvMyOrderDetailsAdditionalNote.text = orderDetails.address.additionalNotes

        if(orderDetails.address.otherDetails.isNotEmpty()){
            binding.tvMyOrderDetailsOtherDetails.visibility = View.VISIBLE
            binding.tvMyOrderDetailsOtherDetails.text = orderDetails.address.otherDetails
        }else {
            binding.tvMyOrderDetailsOtherDetails.visibility = View.GONE
        }

        binding.tvMyOrderDetailsMobileNumber.text = orderDetails.address.phoneNumber
        binding.tvOrderDetailsSubTotal.text = orderDetails.subtotal_amount
        binding.tvOrderDetailsShippingCharge.text = orderDetails.shipping_charge
        binding.tvOrderDetailsTotalAmount.text = orderDetails.total_amount
    }

}





package com.rafalropel.ecommerceshop

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rafalropel.ecommerceshop.adapter.CartItemsAdapter
import com.rafalropel.ecommerceshop.databinding.ActivityCartListBinding
import com.rafalropel.ecommerceshop.firestore.FireStoreClass
import com.rafalropel.ecommerceshop.model.Cart

private lateinit var binding: ActivityCartListBinding

class CartListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
    }


    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarCartListActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back)
        }
        binding.toolbarCartListActivity.setNavigationOnClickListener { onBackPressed() }
    }

    fun successCartItemsList(cartList: ArrayList<Cart>) {
        if (cartList.size > 0) {
            binding.rvCartItemsList.visibility = View.VISIBLE
            binding.llCheckout.visibility = View.VISIBLE
            binding.tvNoCartItemFound.visibility = View.GONE


            binding.rvCartItemsList.layoutManager = LinearLayoutManager(this@CartListActivity)
            binding.rvCartItemsList.setHasFixedSize(true)

            val cartListAdapter = CartItemsAdapter(this@CartListActivity, cartList)
            binding.rvCartItemsList.adapter = cartListAdapter

            var subTotal: Double = 0.0

            for (item in cartList) {
                val price = item.price.toDouble()
                val quantity = item.cart_amount.toInt()

                subTotal += (price * quantity)
            }

            binding.tvSubTotal.text = "$subTotal"
            binding.tvShippingCharge.text = "10"

            if(subTotal > 0){
                binding.llCheckout.visibility = View.VISIBLE
                val total = subTotal + 10
                binding.tvTotalAmount.text = "$total"
            }else{
                binding.llCheckout.visibility = View.GONE
            }

        }else{
            binding.rvCartItemsList.visibility = View.GONE
            binding.llCheckout.visibility = View.GONE
            binding.tvNoCartItemFound.visibility = View.VISIBLE
        }
    }

    private fun getCartItemsList() {
        FireStoreClass().getCartList(this@CartListActivity)
    }

    override fun onResume() {
        super.onResume()
        getCartItemsList()
    }
}
package com.rafalropel.ecommerceshop

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rafalropel.ecommerceshop.adapter.CartItemsAdapter
import com.rafalropel.ecommerceshop.databinding.ActivityCartListBinding
import com.rafalropel.ecommerceshop.firestore.FireStoreClass
import com.rafalropel.ecommerceshop.model.Cart
import com.rafalropel.ecommerceshop.model.Product

private lateinit var binding: ActivityCartListBinding

class CartListActivity : AppCompatActivity() {

    private lateinit var mProductsList: ArrayList<Product>
    private lateinit var mCartListItems: ArrayList<Cart>
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

        for (product in mProductsList) {
            for (cart in cartList) {
                if (product.product_id == cart.product_id) {
                    cart.amount = product.amount

                    if (product.amount.toInt() == 0) {
                        cart.cart_amount = product.amount
                    }
                }
            }
        }

        mCartListItems = cartList


        if (mCartListItems.size > 0) {
            binding.rvCartItemsList.visibility = View.VISIBLE
            binding.llCheckout.visibility = View.VISIBLE
            binding.tvNoCartItemFound.visibility = View.GONE


            binding.rvCartItemsList.layoutManager = LinearLayoutManager(this@CartListActivity)
            binding.rvCartItemsList.setHasFixedSize(true)

            val cartListAdapter = CartItemsAdapter(this@CartListActivity, cartList)
            binding.rvCartItemsList.adapter = cartListAdapter

            var subTotal: Double = 0.0

            for (item in mCartListItems) {

                val availableQuantity = item.amount.toInt()

                if (availableQuantity > 0){
                    val price = item.price.toDouble()
                    val quantity = item.cart_amount.toInt()

                    subTotal += (price * quantity)
                }


            }

            binding.tvSubTotal.text = "$subTotal"
            binding.tvShippingCharge.text = "10"

            if (subTotal > 0) {
                binding.llCheckout.visibility = View.VISIBLE
                val total = subTotal + 10
                binding.tvTotalAmount.text = "$total"
            } else {
                binding.llCheckout.visibility = View.GONE
            }

        } else {
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

        getProductList()
    }

    fun successProductsListFromFirestore(productList: ArrayList<Product>) {
        mProductsList = productList
        getCartItemsList()
    }

    private fun getProductList() {
        FireStoreClass().getAllProductsList(this@CartListActivity)
    }

    fun itemRemoveSuccess(){
        Toast.makeText(this@CartListActivity, getString(R.string.item_removed_success), Toast.LENGTH_SHORT).show()
        getCartItemsList()
    }
    fun updateCartSuccess(){
        getCartItemsList()
    }
}
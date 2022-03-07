package com.rafalropel.ecommerceshop

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rafalropel.ecommerceshop.adapter.CartItemsAdapter
import com.rafalropel.ecommerceshop.databinding.ActivityCheckoutBinding
import com.rafalropel.ecommerceshop.firestore.FireStoreClass
import com.rafalropel.ecommerceshop.model.Address
import com.rafalropel.ecommerceshop.model.Cart
import com.rafalropel.ecommerceshop.model.Order
import com.rafalropel.ecommerceshop.model.Product
import com.rafalropel.ecommerceshop.utils.Constants


private lateinit var binding: ActivityCheckoutBinding

private var mAddressDetails: Address? = null
private lateinit var mProductsList: ArrayList<Product>
private lateinit var mCartItemsList: ArrayList<Cart>
private var mSubtotal: Double = 0.0
private var mTotalAmount: Double = 0.0
private lateinit var mOrderDetails: Order

class CheckoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS)) {
            mAddressDetails = intent.getParcelableExtra(Constants.EXTRA_SELECTED_ADDRESS)
        }

        if (mAddressDetails != null) {
            binding.tvCheckoutAddressType.text = mAddressDetails?.type
            binding.tvCheckoutFullName.text = mAddressDetails?.name
            binding.tvCheckoutAddress.text =
                "${mAddressDetails?.address}, ${mAddressDetails?.zipCode}"
            binding.tvCheckoutAdditionalNote.text = mAddressDetails?.additionalNotes
            binding.tvMobileNumber.text = mAddressDetails?.phoneNumber


            if (mAddressDetails?.otherDetails!!.isNotEmpty()) {
                binding.tvCheckoutOtherDetails.text = mAddressDetails?.otherDetails
            }
        }

        binding.btnPlaceOrder.setOnClickListener {
            placeOrder()
        }
        getProductsList()
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarCheckoutActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        binding.toolbarCheckoutActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getProductsList() {
        FireStoreClass().getAllProductsList(this@CheckoutActivity)
    }

    fun successProductsListFromFirestore(productsList: ArrayList<Product>) {
        mProductsList = productsList
        getCartItemsList()
    }

    private fun getCartItemsList() {
        FireStoreClass().getCartList(this@CheckoutActivity)
    }

    fun successCartItemsList(cartList: ArrayList<Cart>) {
        mCartItemsList = cartList

        for (product in mProductsList) {
            for (cartItem in cartList) {
                if (product.product_id == cartItem.product_id) {
                    cartItem.amount = product.amount
                }
            }
        }

        binding.rvCartListItems.layoutManager = LinearLayoutManager(this)
        binding.rvCartListItems.setHasFixedSize(true)

        var cartListAdapter = CartItemsAdapter(this, mCartItemsList, false)
        binding.rvCartListItems.adapter = cartListAdapter

        for (item in mCartItemsList) {
            val availableQuantity = item.amount.toInt()
            if (availableQuantity > 0) {
                val price = item.price.toDouble()
                val quantity = item.cart_amount.toInt()
                mSubtotal += (price * quantity)
            }
        }

        binding.tvCheckoutSubTotal.text = "$mSubtotal"
        binding.tvCheckoutShippingCharge.text = "10"

        if (mSubtotal > 0) {
            binding.llCheckoutPlaceOrder.visibility = View.VISIBLE
            mTotalAmount = mSubtotal + 10.0
            binding.tvCheckoutTotalAmount.text = "$mTotalAmount"
        } else {
            binding.llCheckoutPlaceOrder.visibility = View.GONE
        }
    }

    private fun placeOrder() {
        if (mAddressDetails != null) {
            mOrderDetails = Order(
                FireStoreClass().getCurrentUserID(),
                mCartItemsList,
                mAddressDetails!!,
                "Zamówienie ${System.currentTimeMillis()}",
                mCartItemsList[0].image,
                mSubtotal.toString(),
                "10",
                mTotalAmount.toString(),
                System.currentTimeMillis()

            )

            FireStoreClass().placeOrder(this, mOrderDetails)
        }

    }

    fun placeOrderSuccess() {
        FireStoreClass().updateAllDetails(this, mCartItemsList, mOrderDetails)
    }
    fun detailsUpdateSuccess(){
        Toast.makeText(this, getString(R.string.place_order_success), Toast.LENGTH_SHORT).show()

        val intent = Intent(this, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
    }

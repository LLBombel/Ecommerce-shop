package com.rafalropel.ecommerceshop

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.rafalropel.ecommerceshop.databinding.ActivityProductDetailsBinding
import com.rafalropel.ecommerceshop.firestore.FireStoreClass
import com.rafalropel.ecommerceshop.model.Cart
import com.rafalropel.ecommerceshop.model.Product
import com.rafalropel.ecommerceshop.utils.Constants
import com.rafalropel.ecommerceshop.utils.GlideLoader


private lateinit var binding: ActivityProductDetailsBinding

class ProductDetailsActivity : BaseActivity() {

    private var mProductId: String = ""
    private lateinit var mProductDetails: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!

        }

        var productOwnerId = ""

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)) {
            productOwnerId = intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
        }

        if (FireStoreClass().getCurrentUserID() == productOwnerId) {
            binding.btnAddToCart.visibility = View.GONE
            binding.btnGoToCart.visibility = View.GONE
        } else {
            binding.btnAddToCart.visibility = View.VISIBLE
        }
        getProductDetails()


        binding.btnAddToCart.setOnClickListener {
            addToCart()
        }

        binding.btnGoToCart.setOnClickListener {
            val intent = Intent(this, CartListActivity::class.java)
            startActivity(intent)
        }

    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarProductDetails)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back)
        }

        binding.toolbarProductDetails.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getProductDetails() {
        FireStoreClass().getProductDetails(this, mProductId)
    }

    fun productDetailsSuccess(product: Product) {
        mProductDetails = product
        GlideLoader(this@ProductDetailsActivity).loadProductPicture(
            product.image, binding.ivProductDetailsImage
        )
        binding.tvProductDetailsTitle.text = product.title
        binding.tvProductDetailsPrice.text = product.price
        binding.tvProductDetailsDescription.text = product.description
        binding.tvProductDetailsAmount.text = product.amount

        if (product.amount.toInt() == 0) {
            binding.btnAddToCart.visibility = View.GONE
            binding.tvProductDetailsAmount.text = getString(R.string.out_of_stock)
            binding.tvProductDetailsAmount.setTextColor(
                ContextCompat.getColor(
                    this@ProductDetailsActivity,
                    R.color.ThemePink
                )
            )
        }else{
            if (FireStoreClass().getCurrentUserID() != product.user_id) {
                FireStoreClass().checkIfItemExistInCart(this, mProductId)
            }
        }


    }

    private fun addToCart() {
        val cartItem = Cart(
            FireStoreClass().getCurrentUserID(),
            mProductId,
            mProductDetails.title,
            mProductDetails.price,
            mProductDetails.image,
            Constants.DEFAULT_CART_QUANTITY
        )

        FireStoreClass().addCartItems(this, cartItem)
    }

    fun addToCartSuccess() {
        Toast.makeText(this, getString(R.string.add_to_cart_success), Toast.LENGTH_SHORT).show()
        binding.btnAddToCart.visibility = View.GONE
        binding.btnGoToCart.visibility = View.VISIBLE
    }

    fun productExistInCart() {
        binding.btnAddToCart.visibility = View.GONE
        binding.btnGoToCart.visibility = View.VISIBLE
    }


}
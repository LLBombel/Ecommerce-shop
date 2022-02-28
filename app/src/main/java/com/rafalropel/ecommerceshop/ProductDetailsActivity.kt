package com.rafalropel.ecommerceshop

import android.os.Bundle
import com.rafalropel.ecommerceshop.databinding.ActivityProductDetailsBinding
import com.rafalropel.ecommerceshop.firestore.FireStoreClass
import com.rafalropel.ecommerceshop.model.Product
import com.rafalropel.ecommerceshop.utils.Constants
import com.rafalropel.ecommerceshop.utils.GlideLoader


private lateinit var binding: ActivityProductDetailsBinding

class ProductDetailsActivity : BaseActivity() {

    private var mProductId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!

        }
        getProductDetails()
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

    private fun getProductDetails(){
        FireStoreClass().getProductDetails(this, mProductId)
    }

    fun productDetailsSuccess(product: Product) {
        GlideLoader(this@ProductDetailsActivity).loadProductPicture(
            product.image, binding.ivProductDetailsImage
        )
        binding.tvProductDetailsTitle.text = product.title
        binding.tvProductDetailsPrice.text = product.price
        binding.tvProductDetailsDescription.text = product.description
        binding.tvProductDetailsAmount.text = product.amount
    }
}
package com.rafalropel.ecommerceshop

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.rafalropel.ecommerceshop.databinding.ActivityAddProductBinding
import com.rafalropel.ecommerceshop.firestore.FireStoreClass
import com.rafalropel.ecommerceshop.model.Product
import com.rafalropel.ecommerceshop.utils.Constants
import com.rafalropel.ecommerceshop.utils.GlideLoader
import java.io.IOException

private lateinit var binding: ActivityAddProductBinding

class AddProductActivity : BaseActivity() {

    private var mSelectedImageFileURI: Uri? = null
    private var mProductImageURL: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarAddProduct)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        binding.toolbarAddProduct.setNavigationOnClickListener { onBackPressed() }


        binding.btnSaveProduct.setOnClickListener {
            if (validateProduct()) {
                uploadProductImage()
                uploadProductDetails()
            }
        }

        binding.ivAddPhoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Constants.imageChooser(this@AddProductActivity)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.imageChooser(this)
            } else {
                showErrorSnackBar(getString(R.string.permission_denied), true)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.IMAGE_REQUEST_CODE) {
                if (data != null) {

                    binding.ivAddPhoto.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_edit
                        )
                    )

                    mSelectedImageFileURI = data.data!!
                    try {
                        GlideLoader(this).loadImage(mSelectedImageFileURI!!, binding.ivProductImage)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@AddProductActivity,
                            getString(R.string.error_loading_image),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun validateProduct(): Boolean {
        return when {

            mSelectedImageFileURI == null -> {
                showErrorSnackBar(getString(R.string.no_product_image), true)
                false
            }

            TextUtils.isEmpty(binding.etProductName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(getString(R.string.no_product_name), true)
                false
            }

            TextUtils.isEmpty(binding.etProductPrice.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(getString(R.string.no_product_price), true)
                false
            }

            TextUtils.isEmpty(binding.etProductAmount.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(getString(R.string.no_product_amount), true)
                false
            }
            TextUtils.isEmpty(binding.etProductDescription.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(getString(R.string.no_product_description), true)
                false
            }
            else -> {
                return true
            }
        }
    }

    private fun uploadProductImage() {
        FireStoreClass().uploadImage(this, mSelectedImageFileURI, Constants.PRODUCT_IMAGE)
    }

    fun imageUploadSuccess(imageURL: String) {
        mProductImageURL = imageURL
        uploadProductDetails()
    }

    fun productUploadSuccess() {
        showErrorSnackBar(getString(R.string.product_added), false)
        finish()
    }

    private fun uploadProductDetails() {
        val username =
            this.getSharedPreferences(Constants.SHOPPOLANDIA_PREFERENES, Context.MODE_PRIVATE)
                .getString(Constants.LOGGED_IN_USERNAME, "")!!
        val product = Product(
            FireStoreClass().getCurrentUserID(),
            username,
            binding.etProductName.text.toString().trim { it <= ' ' },
            binding.etProductPrice.text.toString().trim { it <= ' ' },
            binding.etProductDescription.text.toString().trim { it <= ' ' },
            binding.etProductAmount.text.toString().trim { it <= ' ' },
            mProductImageURL
        )

        FireStoreClass().uploadProduct(this, product)

    }
}
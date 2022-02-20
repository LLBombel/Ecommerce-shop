package com.rafalropel.ecommerceshop

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.rafalropel.ecommerceshop.databinding.ActivityUserProfileBinding
import com.rafalropel.ecommerceshop.firestore.FireStoreClass
import com.rafalropel.ecommerceshop.model.User
import com.rafalropel.ecommerceshop.utils.Constants
import com.rafalropel.ecommerceshop.utils.GlideLoader
import java.io.IOException

private lateinit var binding: ActivityUserProfileBinding
private lateinit var mUserDetails: User
private var mSelectedImageFileUri: Uri? = null
private var mUserProfileImageURL: String = ""

@Suppress("DEPRECATION")
class UserProfileActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN


            )
        }


        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }



        binding.etProfileNamesurname.isEnabled = false
        binding.etProfileNamesurname.setText(mUserDetails.nameSurname)
        binding.etProfileEmail.isEnabled = false
        binding.etProfileEmail.setText(mUserDetails.email)
        binding.etProfilePhone.setText(mUserDetails.phoneNumber.toString())


        binding.ivUserImage.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Constants.imageChooser(this)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }

        }

        binding.btProfileSubmit.setOnClickListener {


            if (validateUserProfileDetails()) {
                if (mSelectedImageFileUri != null) {
                    FireStoreClass().uploadImage(this, mSelectedImageFileUri, Constants.PROFILE_IMAGE)
                } else {
                    updateUserDetails()
                }
            }

        }
    }

    private fun updateUserDetails() {
        val userHashMap = HashMap<String, Any>()
        val phoneNumber = binding.etProfilePhone.text.toString().trim { it <= ' ' }

        val gender = if (binding.rbMale.isChecked) {
            Constants.MALE
        } else {
            Constants.FEMALE
        }

        if (mUserProfileImageURL.isNotEmpty()) {
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
        }

        if (phoneNumber.isNotEmpty()) {
            userHashMap[Constants.PHONE_NUMBER] = phoneNumber.toLong()
        }
        userHashMap[Constants.GENDER] = gender
        userHashMap[Constants.COMPLETE_PROFILE] = 1
        FireStoreClass().updateUser(this, userHashMap)
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
                    try {
                        mSelectedImageFileUri = data.data!!
                        GlideLoader(this).loadImage(mSelectedImageFileUri!!, binding.ivUserImage)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@UserProfileActivity,
                            getString(R.string.error_loading_image),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun validateUserProfileDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etProfilePhone.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(getString(R.string.no_phone_number), true)
                false
            }
            else -> {
                true
            }
        }
    }

    fun userProfileUpdateSuccess() {
        Toast.makeText(this, getString(R.string.profile_updated), Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }

    fun imageUploadSuccess(imageURL: String) {
        mUserProfileImageURL = imageURL
        updateUserDetails()
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarUserProfile)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back)
        }

        binding.toolbarUserProfile.setNavigationOnClickListener { onBackPressed() }
    }
}
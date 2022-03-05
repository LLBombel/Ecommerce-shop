package com.rafalropel.ecommerceshop

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.rafalropel.ecommerceshop.databinding.ActivityAddEditAddressBinding
import com.rafalropel.ecommerceshop.firestore.FireStoreClass
import com.rafalropel.ecommerceshop.model.Address
import com.rafalropel.ecommerceshop.utils.Constants

private lateinit var binding: ActivityAddEditAddressBinding

class AddEditAddressActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()

        binding.btnSubmitAddress.setOnClickListener {
            saveAddressToFirestore()
        }

        binding.rgType.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rb_other) {
                binding.tilOtherDetails.visibility = View.VISIBLE
            } else {
                binding.tilOtherDetails.visibility = View.GONE
            }
        }
    }


    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarAddEditAddressActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back)
        }

        binding.toolbarAddEditAddressActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun validateData(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etFullName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(getString(R.string.please_enter_full_name), true)
                false
            }
            TextUtils.isEmpty(binding.etPhoneNumber.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(getString(R.string.please_enter_phone_number), true)
                false
            }
            TextUtils.isEmpty(binding.etAddress.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(getString(R.string.please_enter_address), true)
                false
            }

            TextUtils.isEmpty(binding.etZipCode.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(getString(R.string.please_enter_zip_code), true)
                false
            }
            else -> {
                true
            }


        }
    }

    private fun saveAddressToFirestore() {
        val fullName: String = binding.etFullName.text.toString().trim { it <= ' ' }
        val phoneNumber: String = binding.etPhoneNumber.text.toString().trim { it <= ' ' }
        val address: String = binding.etAddress.text.toString().trim { it <= ' ' }
        val zipCode: String = binding.etZipCode.text.toString().trim { it <= ' ' }
        val additionalNote: String = binding.etAdditionalNote.text.toString().trim { it <= ' ' }
        val otherDetails: String = binding.etOtherDetails.text.toString().trim { it <= ' ' }

        if (validateData()) {
            val addressType: String = when {
                binding.rbHome.isChecked -> {
                    Constants.HOME
                }

                binding.rbOffice.isChecked -> {
                    Constants.OFFICE
                }
                else -> {
                    Constants.OTHER
                }
            }
            val addressModel = Address(
                FireStoreClass().getCurrentUserID(),
                fullName,
                phoneNumber,
                address,
                zipCode,
                additionalNote,
                addressType,
                otherDetails
            )
            FireStoreClass().addAddress(this, addressModel)
        }


    }

    fun addAddressSuccess() {
        Toast.makeText(this, getString(R.string.address_add_success), Toast.LENGTH_SHORT).show()
        finish()
    }


}
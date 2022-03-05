package com.rafalropel.ecommerceshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.rafalropel.ecommerceshop.adapter.AddressAdapter
import com.rafalropel.ecommerceshop.databinding.ActivityAddressListBinding
import com.rafalropel.ecommerceshop.firestore.FireStoreClass
import com.rafalropel.ecommerceshop.model.Address


private lateinit var binding: ActivityAddressListBinding
class AddressListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
        getAddressList()

        binding.tvAddAddress.setOnClickListener {
            val intent = Intent(this@AddressListActivity, AddEditAddressActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarAddressListActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back)
        }

        binding.toolbarAddressListActivity.setNavigationOnClickListener { onBackPressed() }
    }


    fun successAddressListFromFirestore(addressList: ArrayList<Address>){
        if(addressList.size > 0 ){
            binding.tvNoAddressFound.visibility = View.GONE
            binding.rvAddressList.visibility = View.VISIBLE

            binding.rvAddressList.layoutManager = LinearLayoutManager(this)
            binding.rvAddressList.setHasFixedSize(true)

            val addressAdapter = AddressAdapter(this, addressList)

            binding.rvAddressList.adapter = addressAdapter

        }else{
            binding.tvNoAddressFound.visibility = View.VISIBLE
            binding.rvAddressList.visibility = View.GONE
        }
    }

    private fun getAddressList(){
        FireStoreClass().getAddressesList(this)
    }

    override fun onResume() {
        super.onResume()
        getAddressList()
    }
}
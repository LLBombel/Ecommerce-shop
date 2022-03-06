package com.rafalropel.ecommerceshop

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rafalropel.ecommerceshop.adapter.AddressAdapter
import com.rafalropel.ecommerceshop.databinding.ActivityAddressListBinding
import com.rafalropel.ecommerceshop.firestore.FireStoreClass
import com.rafalropel.ecommerceshop.model.Address
import com.rafalropel.ecommerceshop.utils.SwipeToDeleteCallback
import com.rafalropel.ecommerceshop.utils.SwipeToEditCallback


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


    fun successAddressListFromFirestore(addressList: ArrayList<Address>) {
        if (addressList.size > 0) {
            binding.tvNoAddressFound.visibility = View.GONE
            binding.rvAddressList.visibility = View.VISIBLE

            binding.rvAddressList.layoutManager = LinearLayoutManager(this)
            binding.rvAddressList.setHasFixedSize(true)

            val addressAdapter = AddressAdapter(this, addressList)

            binding.rvAddressList.adapter = addressAdapter

            val editSwipeHandler = object : SwipeToEditCallback(this) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val adapter = binding.rvAddressList.adapter as AddressAdapter
                    adapter.notifyEditItem(this@AddressListActivity, viewHolder.adapterPosition)

                }
            }
            val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)

            editItemTouchHelper.attachToRecyclerView(binding.rvAddressList)

            val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    FireStoreClass().deleteAddress(
                        this@AddressListActivity,
                        addressList[viewHolder.adapterPosition].id
                    )
                }
            }

            val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
            deleteItemTouchHelper.attachToRecyclerView(binding.rvAddressList)


        } else {
            binding.tvNoAddressFound.visibility = View.VISIBLE
            binding.rvAddressList.visibility = View.GONE
        }
    }

    private fun getAddressList() {
        FireStoreClass().getAddressesList(this)
    }

    override fun onResume() {
        super.onResume()
        getAddressList()
    }

    fun deleteAddressSuccess() {
        Toast.makeText(this, getString(R.string.address_delete_success), Toast.LENGTH_SHORT).show()
        getAddressList()
    }
}
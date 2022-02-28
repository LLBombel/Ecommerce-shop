package com.rafalropel.ecommerceshop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rafalropel.ecommerceshop.databinding.ActivityCartListBinding

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
}
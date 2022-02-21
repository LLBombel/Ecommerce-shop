package com.rafalropel.ecommerceshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rafalropel.ecommerceshop.databinding.ActivityProductDetailsBinding


private lateinit var binding: ActivityProductDetailsBinding
class ProductDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}
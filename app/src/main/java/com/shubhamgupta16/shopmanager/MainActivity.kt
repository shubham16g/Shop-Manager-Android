package com.shubhamgupta16.shopmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shubhamgupta16.shopmanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.products.setOnClickListener {
            startActivity(Intent(this, ProductsActivity::class.java))
        }
        binding.sellProduct.setOnClickListener {
            startActivity(Intent(this, SellActivity::class.java))
        }

    }
}
package com.shubhamgupta16.shopmanager

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubhamgupta16.shopmanager.adapter.SearchProductAdapter
import com.shubhamgupta16.shopmanager.adapter.SellProductAdapter
import com.shubhamgupta16.shopmanager.common.inr
import com.shubhamgupta16.shopmanager.databinding.ActivitySellBinding
import com.shubhamgupta16.shopmanager.models.ProductModel
import com.shubhamgupta16.shopmanager.models.SellModel
import com.shubhamgupta16.shopmanager.room.AppDatabase.Companion.db
import com.shubhamgupta16.shopmanager.room.ProductDao

class SellActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySellBinding
    private lateinit var productDao: ProductDao

    private val searchList = ArrayList<ProductModel>()
    private lateinit var searchAdapter: SearchProductAdapter

    private val sellList = ArrayList<SellModel>()
    private lateinit var sellProductAdapter: SellProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellBinding.inflate(layoutInflater)
        setContentView(binding.root)
        productDao = db.productDao()

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { filterSearch(it) }
                return true
            }
        })

        setupSearchRecycler()
        setupSellRecycler()

        binding.billingButton.setOnClickListener {
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateBillingButtonData(){
        if (sellList.isEmpty()){
            binding.billingButton.visibility = View.GONE
        } else {
            val totalAmount: Double = sellList.sumOf { it.amount.toDouble() * it.quantity }
            val totalRate: Double = sellList.sumOf { it.rate.toDouble() * it.quantity }
            binding.grandTotal.text = totalAmount.inr
            binding.grandTotalInfo.text = "${totalRate.inr} + GST"
            binding.billingButton.visibility = View.VISIBLE
        }
    }

    private fun setupSellRecycler() {
        sellProductAdapter = SellProductAdapter(sellList){
            updateBillingButtonData()
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = sellProductAdapter
    }

    private fun filterSearch(query: String) {
        searchList.clear()
        if (query.isNotEmpty()) {
            searchList.addAll(productDao.searchProducts(query))
        }
        searchAdapter.notifyDataSetChanged()
        binding.searchRecyclerView.visibility = if (searchList.isEmpty())
            View.GONE else View.VISIBLE
    }

    private fun setupSearchRecycler() {
        searchAdapter = SearchProductAdapter(searchList) {
            val model = sellList.firstOrNull { sellModel -> sellModel.id == it.id }
            if (model == null) {
                sellList.add(SellModel(it.id, it.name, it.amount, it.gst))
                sellProductAdapter.notifyItemInserted(sellList.lastIndex)
                binding.searchView.setQuery("", false)
                updateBillingButtonData()
            } else{
                Toast.makeText(this, "Item Already Selected!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.searchRecyclerView.adapter = searchAdapter
    }
}
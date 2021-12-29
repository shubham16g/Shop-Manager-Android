package com.shubhamgupta16.shopmanager

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubhamgupta16.shopmanager.adapter.SearchProductAdapter
import com.shubhamgupta16.shopmanager.databinding.ActivityProductsBinding
import com.shubhamgupta16.shopmanager.models.ProductModel
import com.shubhamgupta16.shopmanager.room.AppDatabase.Companion.db
import com.shubhamgupta16.shopmanager.room.ProductDao

class ProductsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductsBinding
    private lateinit var productDao: ProductDao

    private val searchList = ArrayList<ProductModel>()
    private lateinit var searchAdapter: SearchProductAdapter
    private var query = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        productDao = db.productDao()

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.fab.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                query = it
                    filterSearch()
                }
                return true
            }
        })

        setupSearchRecycler()
    }

    override fun onStart() {
        super.onStart()
        filterSearch()
    }

    private fun filterSearch() {
        searchList.clear()
        if (query.isNotEmpty()) {
            searchList.addAll(productDao.searchProducts(query))
        } else {
            searchList.addAll(productDao.getAllProducts())
        }
        searchAdapter.notifyDataSetChanged()
        binding.recyclerView.visibility = if (searchList.isEmpty())
            View.GONE else View.VISIBLE
    }

    private fun setupSearchRecycler() {
        searchAdapter = SearchProductAdapter(searchList) {
            /*val model = sellList.firstOrNull { sellModel -> sellModel.id == it.id }
            if (model == null) {
                sellList.add(SellModel(it.id, it.name, it.amount, it.gst))
                sellProductAdapter.notifyItemInserted(sellList.lastIndex)
                binding.searchView.setQuery("", false)
                updateBillingButtonData()
            } else{
                Toast.makeText(this, "Item Already Selected!", Toast.LENGTH_SHORT).show()
            }*/
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = searchAdapter
    }
}
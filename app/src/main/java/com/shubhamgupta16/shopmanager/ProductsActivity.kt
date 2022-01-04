package com.shubhamgupta16.shopmanager

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubhamgupta16.shopmanager.adapter.ProductAdapter
import com.shubhamgupta16.shopmanager.databinding.ActivityProductsBinding
import com.shubhamgupta16.shopmanager.models.ProductModel
import com.shubhamgupta16.shopmanager.room.AppDatabase.Companion.db
import com.shubhamgupta16.shopmanager.room.ProductDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductsBinding
    private lateinit var productDao: ProductDao

    private val productList = ArrayList<ProductModel>()
    private lateinit var productAdapter: ProductAdapter
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
        productList.clear()
        val liveData = if (query.isNotEmpty()) productDao.searchProducts(query)
        else productDao.getAllProducts()
        liveData.observe(this) {
            it?.let {
                productList.addAll(it)
                productAdapter.notifyDataSetChanged()
                binding.recyclerView.visibility = if (productList.isEmpty())
                    View.GONE else View.VISIBLE
            }
        }
    }

    private fun setupSearchRecycler() {
        productAdapter = ProductAdapter(this, productList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = productAdapter
    }
}
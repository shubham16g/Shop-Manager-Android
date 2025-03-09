package com.shubhamgupta16.shopmanager

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubhamgupta16.shopmanager.adapter.SearchProductAdapter
import com.shubhamgupta16.shopmanager.adapter.SellProductAdapter
import com.shubhamgupta16.shopmanager.common.RecyclerItem
import com.shubhamgupta16.shopmanager.common.inr
import com.shubhamgupta16.shopmanager.databinding.ActivitySellBinding
import com.shubhamgupta16.shopmanager.models.SellModel
import com.shubhamgupta16.shopmanager.viewmodels.SellViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SellActivity : AppCompatActivity() {

    private val sellViewModel: SellViewModel by viewModels()
    private lateinit var binding: ActivitySellBinding

    private lateinit var searchAdapter: SearchProductAdapter
    private lateinit var sellProductAdapter: SellProductAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()

//        observers
        sellViewModel.liveSearchList.observe(this) {
            searchAdapter.notifyDataSetChanged()
            binding.searchRecyclerView.visibility = if (sellViewModel.searchList.isEmpty())
                View.GONE else View.VISIBLE
        }
        sellViewModel.liveCartList.observe(this) {
            when (it.case) {
                RecyclerItem.CASE_INSERTED -> {
                    sellProductAdapter.notifyItemInserted(it.position)
                    binding.searchView.setQuery("", false)
                }
                RecyclerItem.CASE_REMOVED -> {
                    sellProductAdapter.notifyItemRemoved(it.position)
                }
            }
            sellViewModel.updateTotalPrice()
        }
        sellViewModel.liveTotalPrice.observe(this) {
            if (it == null || it[0] == 0.0) {
                binding.billingButton.visibility = View.GONE
            } else {
                binding.grandTotal.text = it[0].inr
                binding.grandTotalInfo.text = "${it[0].inr} + GST"
                binding.billingButton.visibility = View.VISIBLE
            }
        }
    }

    private fun setupViews() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                sellViewModel.search(newText)
                return true
            }
        })

        binding.billingButton.setOnClickListener {
            val intent = Intent(this, PrintWebActivity::class.java)
            startActivity(intent)
        }

        searchAdapter = SearchProductAdapter(this, sellViewModel.searchList) {
            val model = sellViewModel.cartList.firstOrNull { sellModel -> sellModel.id == it.id }
            if (model == null) {
                sellViewModel.addNewProduct(SellModel(it.id, it.name, it.amount, it.gst))
            } else {
                Toast.makeText(this, "Item Already Selected!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.searchRecyclerView.adapter = searchAdapter

//        sell product adapter
        sellProductAdapter = SellProductAdapter(this, sellViewModel.cartList, {
            sellViewModel.updateTotalPrice()
        }, {
            sellViewModel.removeProduct(it)
        })
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
        binding.recyclerView.adapter = sellProductAdapter
    }
}
package com.shubhamgupta16.shopmanager.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shubhamgupta16.shopmanager.common.inr
import com.shubhamgupta16.shopmanager.databinding.ItemSearchLayoutBinding
import com.shubhamgupta16.shopmanager.models.ProductModel

class SearchProductAdapter(private val list: ArrayList<ProductModel>, private val listener: (ProductModel)->Unit) :
    RecyclerView.Adapter<SearchProductAdapter.ItemViewHolder>() {

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val model = list[position]
        holder.binding.productName.text = model.name
        holder.binding.productAmount.text = model.amount.inr
        holder.binding.root.setOnClickListener {
            listener(model)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(
            ItemSearchLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount() = list.size

    inner class ItemViewHolder(val binding: ItemSearchLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

}
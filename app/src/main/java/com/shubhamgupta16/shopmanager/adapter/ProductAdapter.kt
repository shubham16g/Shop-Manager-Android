package com.shubhamgupta16.shopmanager.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shubhamgupta16.shopmanager.AddProductActivity
import com.shubhamgupta16.shopmanager.common.inr
import com.shubhamgupta16.shopmanager.common.showDeleteProductAlert
import com.shubhamgupta16.shopmanager.databinding.ItemProductLayoutBinding
import com.shubhamgupta16.shopmanager.models.ProductModel

class ProductAdapter(private val context: Context, private val list: ArrayList<ProductModel>) :
    RecyclerView.Adapter<ProductAdapter.ItemViewHolder>() {

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val model = list[position]
        holder.binding.productName.text = model.name
        holder.binding.productAmount.text = model.amount.inr
        holder.binding.productAmountInfo.text =
            if (model.gst > 0) "${model.rate.inr} + ${model.displayGst} GST" else ""

        holder.binding.editButton.setOnClickListener {
            val intent = Intent(context, AddProductActivity::class.java)
            intent.putExtra("model", model)
            context.startActivity(intent)
        }
        holder.binding.deleteButton.setOnClickListener {
            context.showDeleteProductAlert(model){
                list.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemCount - position)
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(
            ItemProductLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount() = list.size

    inner class ItemViewHolder(val binding: ItemProductLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

}
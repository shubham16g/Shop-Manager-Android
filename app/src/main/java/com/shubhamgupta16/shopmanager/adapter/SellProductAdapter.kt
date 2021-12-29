package com.shubhamgupta16.shopmanager.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shubhamgupta16.shopmanager.common.inr
import com.shubhamgupta16.shopmanager.databinding.ItemSellLayoutBinding
import com.shubhamgupta16.shopmanager.models.SellModel

class SellProductAdapter(private val list: ArrayList<SellModel>, private val quantityChangeListener: ()->Unit) :
    RecyclerView.Adapter<SellProductAdapter.ItemViewHolder>() {

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val model = list[position]
        holder.binding.productName.text = model.name
        holder.binding.productAmount.text = model.amount.inr
        holder.binding.productAmountInfo.text = if (model.gst > 0) "${model.rate.inr} + ${model.displayGst} GST" else ""
        holder.binding.addCartButton.count = model.quantity
        holder.binding.productTotalAmount.text = (model.amount * model.quantity).inr
        holder.binding.addCartButton.onCountChangeListener = {
            if (it == 0){
                list.removeAt(position)
                notifyItemRemoved(position)
                quantityChangeListener()
                false
            } else {
                list[position].quantity = it
                holder.binding.productTotalAmount.text = (model.amount * it).inr
                quantityChangeListener()
                true
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(
            ItemSellLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount() = list.size

    inner class ItemViewHolder(val binding: ItemSellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

}
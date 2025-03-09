package com.shubhamgupta16.shopmanager.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shubhamgupta16.shopmanager.common.showEditAmountDialog
import com.shubhamgupta16.shopmanager.common.inr
import com.shubhamgupta16.shopmanager.databinding.ItemSellLayoutBinding
import com.shubhamgupta16.shopmanager.models.SellModel

class SellProductAdapter(
    private val context: Context,
    private val list: List<SellModel>,
    private val quantityChangeListener: () -> Unit,
    private val removeItemListener: (position:Int) -> Unit
) :
    RecyclerView.Adapter<SellProductAdapter.ItemViewHolder>() {

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val model = list[position]
        holder.binding.productName.text = model.name
        holder.binding.productAmount.text = model.amount.inr
        holder.binding.productAmountInfo.text =
            if (model.gst > 0) "${model.rate.inr} + ${model.displayGst} GST" else ""
        holder.binding.addCartButton.count = model.quantity
        holder.binding.productTotalAmount.text = (model.amount * model.quantity).inr
        holder.binding.addCartButton.onCountChangeListener = {
            if (it == 0) {
                if (list.isNotEmpty()) {
                    removeItemListener(position)
                }
                false
            } else {
                list[position].quantity = it
                holder.binding.productTotalAmount.text = (model.amount * it).inr
                quantityChangeListener()
                true
            }
        }
        holder.binding.productAmount.setOnClickListener {
            editPrice(position)
        }
    }

    private fun editPrice(position: Int) {
        val model = list[position]
        context.showEditAmountDialog(model) { s, _, _ ->
            list[position].amount = s.toFloat()
            notifyItemChanged(position)
            quantityChangeListener()
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

    interface OnSellItemListener{
        fun updateQuantity(quantity:Int, position: Int)
        fun updateAmount(amount:Double, position: Int)
    }

}
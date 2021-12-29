package com.shubhamgupta16.shopmanager.models


data class SellModel(
    val id: Int,
    val name: String,
    val amount: Float,
    val gst: Float,
    var quantity: Int = 1
) {
    val rate
        get() = amount * (100f - gst) / 100f

    val displayGst: String
        get() {
            val gstStr = gst.toString()
            return if (gstStr.endsWith(".0")) gst.toString().replace(".0", "") + "%" else "$gstStr%"
        }
}
package com.shubhamgupta16.shopmanager.models

import androidx.room.*
import java.io.Serializable
import java.text.NumberFormat
import java.time.temporal.TemporalAmount
import java.util.*

@Entity(tableName = "products")
data class ProductModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val description: String,
    val amount:  Float,
    val gst: Float,
    val gstExcluded: Boolean,
    val status: Int
):Serializable{
    val rate
        get() = amount * (100f - gst) / 100f

    val displayGst: String
        get() {
            val gstStr = gst.toString()
            return if (gstStr.endsWith(".0")) gst.toString().replace(".0", "") + "%" else "$gstStr%"
        }
}
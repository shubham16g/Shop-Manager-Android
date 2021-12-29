package com.shubhamgupta16.shopmanager.room

import androidx.room.*
import java.time.temporal.TemporalAmount

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
)
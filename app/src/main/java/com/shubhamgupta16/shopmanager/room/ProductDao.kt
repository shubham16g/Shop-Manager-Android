package com.shubhamgupta16.shopmanager.room

import androidx.room.*
import com.shubhamgupta16.shopmanager.models.ProductModel

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE name LIKE '%' || :path || '%' ORDER BY (CASE WHEN name = :path THEN 1 WHEN name LIKE :path || '%' THEN 2 ELSE 3 END),name LIMIT 10")
    fun searchProducts(path: String): List<ProductModel>

    @Query("SELECT * FROM products")
    fun getAllProducts(): List<ProductModel>

    @Query("DELETE FROM products")
    fun deleteAllFiles()

    @Insert
    fun insert(product: ProductModel)

    @Delete
    fun delete(product: ProductModel)
}
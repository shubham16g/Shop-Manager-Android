package com.shubhamgupta16.shopmanager.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.shubhamgupta16.shopmanager.models.ProductModel

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE name LIKE '%' || :path || '%' ORDER BY (CASE WHEN name = :path THEN 1 WHEN name LIKE :path || '%' THEN 2 ELSE 3 END),name LIMIT 10")
    fun searchProducts(path: String):LiveData<List<ProductModel>>

    @Query("SELECT * FROM products")
    fun getAllProducts(): LiveData<List<ProductModel>>

    @Query("DELETE FROM products")
    suspend fun deleteAllFiles()

    @Insert
    suspend fun insert(product: ProductModel)

    @Update
    suspend fun update(product: ProductModel)

    @Delete
    suspend fun delete(product: ProductModel)
}
package com.shubhamgupta16.shopmanager.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shubhamgupta16.shopmanager.models.ProductModel
import com.shubhamgupta16.shopmanager.room.ProductDao
import javax.inject.Inject

class SellRepository @Inject constructor(private val productDao: ProductDao) {
    fun searchProducts(path: String?): LiveData<List<ProductModel>> {
        if (path.isNullOrEmpty())
            return MutableLiveData(null)
        return productDao.searchProducts(path)
    }

    fun getAllProducts(): LiveData<List<ProductModel>> = productDao.getAllProducts()

    suspend fun deleteAllFiles() = productDao.deleteAllFiles()

    suspend fun insert(product: ProductModel) = productDao.insert(product)

    suspend fun update(product: ProductModel) = productDao.update(product)

    suspend fun delete(product: ProductModel) = productDao.delete(product)
}
package com.shubhamgupta16.shopmanager.room

import android.content.Context
import androidx.room.*
import com.shubhamgupta16.shopmanager.models.ProductModel

@Database(entities = [ProductModel::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        val Context.db: AppDatabase
            get() = Room.databaseBuilder(
                this,
                AppDatabase::class.java, "database-name2"
            ).build()
    }
}
package com.shubhamgupta16.shopmanager.room

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.shubhamgupta16.shopmanager.models.ProductModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.reflect.KParameter

@Database(entities = [ProductModel::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val Context.db: AppDatabase
            get() = INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    this, AppDatabase::class.java, "shop-db-name"
                ).addCallback(roomCallback).build()
                INSTANCE = instance
                instance
            }

        @OptIn(DelicateCoroutinesApi::class)
        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                GlobalScope.launch(Dispatchers.IO) { // Insert default rows on background thread
                    INSTANCE?.let { database ->
                        val userDao = database.productDao()
                        userDao.insert(
                            ProductModel(
                                1, "Sample Product", "", 100.0f, 10.0f, false, 1
                            )
                        )
                        userDao.insert(
                            ProductModel(
                                2, "Sample Product 2", "", 200.0f, 20.0f, false, 1
                            )
                        )

                    }
                }
            }
        }
    }
}
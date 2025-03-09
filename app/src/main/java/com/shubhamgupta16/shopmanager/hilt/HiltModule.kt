package com.shubhamgupta16.shopmanager.hilt

import android.content.Context
import com.shubhamgupta16.shopmanager.room.AppDatabase.Companion.db
import com.shubhamgupta16.shopmanager.room.ProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HiltModule {

    @Provides
    @Singleton
    fun providesProductDao(@ApplicationContext context: Context): ProductDao {
        return context.db.productDao()
    }
}
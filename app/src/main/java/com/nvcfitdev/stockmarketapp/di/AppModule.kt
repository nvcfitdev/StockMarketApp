package com.nvcfitdev.stockmarketapp.di

import android.app.Application
import androidx.room.Room
import com.nvcfitdev.stockmarketapp.data.local.StockDatabase
import com.nvcfitdev.stockmarketapp.data.remote.StockService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideStockService(): StockService {
        return Retrofit.Builder().baseUrl(StockService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StockService::class.java)
    }

    @Provides
    @Singleton
    fun provideStockDatabase(app: Application): StockDatabase {
        return Room.databaseBuilder(
            app,
            StockDatabase::class.java,
            "stockdb.db"
        ).build()
    }
}
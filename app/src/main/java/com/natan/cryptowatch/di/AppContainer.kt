package com.natan.cryptowatch.di

import android.content.Context
import androidx.room.Room
import com.natan.cryptowatch.data.local.AppDatabase
import com.natan.cryptowatch.data.remote.CoinGeckoApi
import com.natan.cryptowatch.data.repository.CoinRepository
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object AppContainer {
    fun repository(context: Context): CoinRepository {
        val db = Room.databaseBuilder(context, AppDatabase::class.java, "crypto-watch.db").build()
        val api = Retrofit.Builder().baseUrl("https://api.coingecko.com/").addConverterFactory(MoshiConverterFactory.create()).build().create(CoinGeckoApi::class.java)
        return CoinRepository(api, db.favoriteDao())
    }
}

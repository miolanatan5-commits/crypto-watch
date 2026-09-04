package com.natan.cryptowatch.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao interface FavoriteDao {
    @Query("SELECT * FROM favorite_coins") fun observeAll(): Flow<List<FavoriteCoin>>
    @Upsert suspend fun upsert(coin: FavoriteCoin)
    @Query("DELETE FROM favorite_coins WHERE id = :id") suspend fun delete(id: String)
}

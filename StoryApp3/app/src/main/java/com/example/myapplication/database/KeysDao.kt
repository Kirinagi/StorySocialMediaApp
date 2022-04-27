package com.example.myapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.api.Keys

@Dao
interface KeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<Keys>)


    @Query("SELECT * FROM keys WHERE id = :id")
    suspend fun getRemoteKeysId(id: String): Keys?


    @Query("DELETE FROM keys")
    suspend fun deleteKeys()
}
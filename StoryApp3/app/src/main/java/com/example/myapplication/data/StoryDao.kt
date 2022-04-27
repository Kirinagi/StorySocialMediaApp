package com.example.myapplication.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.api.ListStory

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<ListStory>)


    @Query("SELECT * FROM liststory")
    fun getAllStory(): PagingSource<Int, ListStory>


    @Query("SELECT * FROM liststory")
    suspend fun findAll(): List<ListStory>


    @Query("DELETE FROM liststory")
    suspend fun deleteAll()
}
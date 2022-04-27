package com.example.myapplication.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.myapplication.api.ApiService
import com.example.myapplication.api.ListStory
import com.example.myapplication.database.StoryDatabase

class StoryRepo(private val storyDatabase: StoryDatabase?, private val apiService: ApiService) {

    @ExperimentalPagingApi
    fun find(token: String): LiveData<PagingData<ListStory>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = storyDatabase?.let { RemoteMediator(it, apiService, token) },
            pagingSourceFactory = {
                storyDatabase?.StoryDao()!!.getAllStory()
            }
        ).liveData
    }


    suspend fun getData(): List<ListStory> {
        return storyDatabase?.StoryDao()!!.findAll()
    }
}
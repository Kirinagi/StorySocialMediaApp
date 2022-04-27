package com.example.myapplication.di

import android.content.Context
import com.example.myapplication.api.ApiConfig
import com.example.myapplication.data.StoryRepo
import com.example.myapplication.database.StoryDatabase

object Injection {

    fun provideRepo(context: Context?): StoryRepo {
        val database = context?.let { StoryDatabase.getDatabase(it) }
        val apiService = ApiConfig.getApiService()
        return StoryRepo(database, apiService)
    }
}
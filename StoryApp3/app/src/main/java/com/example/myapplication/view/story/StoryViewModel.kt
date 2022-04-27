package com.example.myapplication.view.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myapplication.UserPreference
import com.example.myapplication.api.ListStory
import com.example.myapplication.data.MainActStories
import com.example.myapplication.data.PagingStories
import com.example.myapplication.data.StoryRepo
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class StoryViewModel(private val pref: UserPreference, private val storyRepo: StoryRepo) :
    ViewModel() {

    fun getUser(): LiveData<PagingStories>{
        return pref.getUser().asLiveData()
    }


    suspend fun getData(): List<ListStory>{
        return storyRepo.getData()
    }


    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }


    val pagingStory: (String) -> LiveData<PagingData<ListStory>> = { token: String ->
        storyRepo.find("Bearer $token").cachedIn(viewModelScope)
    }
}
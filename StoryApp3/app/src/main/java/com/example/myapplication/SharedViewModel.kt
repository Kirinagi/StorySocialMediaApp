package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.MainActStories
import com.example.myapplication.data.PagingStories
import kotlinx.coroutines.launch

class SharedViewModel(private val pref: UserPreference) : ViewModel() {
    fun getUser(): LiveData<PagingStories> {
        return pref.getUser().asLiveData()
    }


    fun saveUser(user: MainActStories) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }


    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}
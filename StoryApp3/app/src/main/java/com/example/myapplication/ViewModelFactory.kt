package com.example.myapplication

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.example.myapplication.di.Injection
import com.example.myapplication.view.story.StoryViewModel


@ExperimentalPagingApi
class ViewModelFactory(private val pref: UserPreference, private val context: Context?) : ViewModelProvider.NewInstanceFactory() {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SharedViewModel::class.java) -> {
                SharedViewModel(pref) as T
            }

            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                return StoryViewModel(pref,Injection.provideRepo(context)) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}
package com.example.myapplication.data

import android.os.Parcelable
import com.example.myapplication.api.ListStory
import kotlinx.parcelize.Parcelize

data class ResponseLogin(
    val loginResult: LoginResult,
    val error: Boolean,
    val message: String
)


data class ResponseRegister(
    val error: Boolean,
    val message: String
)


data class LoginResult(
    val name: String,
    val userId: String,
    val token: String
)


data class ResponseUpload(
    val error: Boolean,
    val message: String
)


@Parcelize
data class Story(
    var name: String? = null,
    var photo: String? = null,
    var description: String? = null
) : Parcelable


@Parcelize
data class MainActStories(
    val token: String,
    val isLogin: Boolean
) : Parcelable


data class PagingStories(
    var name: String,
    var token: String,
    var isLogin: Boolean
)


data class ResponseStories(
    val listStory: List<ListStory>,
    val error: Boolean,
    val message: String
)

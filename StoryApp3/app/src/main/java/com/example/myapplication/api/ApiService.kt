package com.example.myapplication.api

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.data.ResponseLogin
import com.example.myapplication.data.ResponseRegister
import com.example.myapplication.data.ResponseStories
import com.example.myapplication.data.ResponseUpload
import kotlinx.parcelize.Parcelize
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

@Entity
@Parcelize
data class ListStory(
    val photoUrl: String,
    val createdAt: String,
    val name: String,
    val description: String,
    val lon: Double,
    @PrimaryKey
    val id: String,
    val lat: Double
): Parcelable


interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun createAccount(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseRegister>


    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseLogin>


    @GET("stories")
   suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int? = null
    ): ResponseStories


    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?
    ): Call<ResponseUpload>
}


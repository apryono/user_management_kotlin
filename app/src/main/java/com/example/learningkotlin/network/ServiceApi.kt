package com.example.learningkotlin.network

import com.example.learningkotlin.model.SetterGetter.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ServiceApi {

    @POST("/login")
    fun login(@Body request: RequestBody) : Call<Login>

    @POST("/register")
    fun register(@Body request: RequestBody) : Call<Register>

    @GET("/user")
    fun getUser(@Header("Authorization") token:String) : Call<ListUsers>

    @GET("/user/{id}")
    fun getUserById(@Header("Authorization") token: String, @Path("id") id: Int) : Call<UserById>

    @PUT("/user/{id}")
    fun updateUser(@Header("Authorization") token: String, @Body request: RequestBody, @Path("id") id:Int) : Call<Update>

    @DELETE("/user/{id}")
    fun delete(@Header("Authorization") token: String, @Path("id") id: Int): Call<DefaultResponse>




}
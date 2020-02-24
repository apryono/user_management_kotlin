package com.example.learningkotlin.network

import com.example.learningkotlin.BuildConfig
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitBuilder {
    companion object {
        private var URL_BASE = "http://10.10.17.187"
        private var BASE_URL_LOGIN = URL_BASE + ":8000"

        fun Login(): Retrofit {
            val client = okhttp3.OkHttpClient().newBuilder().addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
            })
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL_LOGIN)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        }
    }
}
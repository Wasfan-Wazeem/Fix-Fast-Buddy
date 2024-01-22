package com.wasfan.fixfastbuddy

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val baseURL = "http://10.21.131.174/Fix-Fast-Buddy/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: RetrofitAPI by lazy {
        retrofit.create(RetrofitAPI::class.java)
    }
}
package com.wasfan.fixfastbuddy

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val baseURL = "http://192.168.43.67/Fix-Fast-Buddy/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
    }

    val api: RetrofitAPI by lazy {
        retrofit.create(RetrofitAPI::class.java)
    }
}
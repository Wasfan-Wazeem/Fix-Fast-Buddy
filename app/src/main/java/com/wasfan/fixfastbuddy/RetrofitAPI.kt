package com.wasfan.fixfastbuddy

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitAPI {

    @GET("read.php")
    fun getUsers(): Call<List<Users>>

    @FormUrlEncoded
    @POST("AddUser.php")
    fun createUser(
        @Field("fName") firstName: String,
        @Field("lName") lastName: String,
        @Field("Email") email: String,
        @Field("PNumber") phoneNumber: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("AddVehicle.php")
    fun createVehicle(
        @Field("make") make: String,
        @Field("model") model: String,
        @Field("year") year: String,
        @Field("type") type: String,
        @Field("license") license: String,
        @Field("insurance") insurance: String,
        @Field("PNumber") phoneNumber: String
    ): Call<ResponseBody>


}
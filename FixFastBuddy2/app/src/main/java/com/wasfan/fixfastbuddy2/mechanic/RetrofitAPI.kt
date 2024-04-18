package com.wasfan.fixfastbuddy2.mechanic

import com.wasfan.fixfastbuddy2.mechanic.DataClass.NearbyServiceRequestResponseDataClass
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitAPI {

    @FormUrlEncoded
    @POST("CheckMechanic.php")
    fun checkMechanic(@Field("phoneNumber") phoneNumber: String): Call<Boolean>

    @FormUrlEncoded
    @POST("CheckGarageMechanic.php")
    fun checkGarageMechanic(@Field("phoneNumber") phoneNumber: String): Call<Boolean>

    //Create
    @FormUrlEncoded
    @POST("AddMechanic.php")
    fun createMechanic(
        @Field("fName") firstName: String,
        @Field("lName") lastName: String,
        @Field("PNumber") phoneNumber: String,
        @Field("dateJoined") dateJoined: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("updateLocation.php")
    fun updateMechanicLocation(
        @Field("mechanicPhoneNumber") mechanicPhoneNumber: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String
    ): Call<ResponseBody>

    @GET("FetchNearbyMechanics.php") // Adjust with your actual PHP script endpoint
    fun getNearbyServiceRequests(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double,
        @Query("radius") radius: Double
    ): Call<NearbyServiceRequestResponseDataClass>
}
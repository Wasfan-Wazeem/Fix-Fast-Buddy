package com.wasfan.fixfastbuddy2.mechanic

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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
    @POST("update_location.php")
    fun updateMechanicLocation(
        @Field("mechanic_id") mechanic_id: String,
        @Field("latLng") latLng: String
    ): Call<ResponseBody>
}
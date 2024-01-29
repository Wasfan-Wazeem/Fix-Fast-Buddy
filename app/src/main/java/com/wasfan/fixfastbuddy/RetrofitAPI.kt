package com.wasfan.fixfastbuddy

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.Date

interface RetrofitAPI {
    //Read
    @FormUrlEncoded
    @POST("GetUserDetails.php")
    fun getUserDetails(@Field("phoneNumber") phoneNumber: String): Call<Users>

    @FormUrlEncoded
    @POST("GetVehicleDetails.php")
    fun getVehicleDetails(@Field("phoneNumber") phoneNumber: String): Call<List<Vehicles>>

    @FormUrlEncoded
    @POST("checkPhoneNum.php")
    fun checkPhoneNumber(@Field("phoneNumber") phoneNumber: String): Call<Boolean>

    //Create
    @FormUrlEncoded
    @POST("AddUser.php")
    fun createUser(
        @Field("fName") firstName: String,
        @Field("lName") lastName: String,
        @Field("Email") email: String,
        @Field("PNumber") phoneNumber: String,
        @Field("dateJoined") dateJoined: String
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

    //Update
    @FormUrlEncoded
    @POST("UpdateUserDetails.php")
    fun updateUserDetails(
        @Field("phoneNumber") phoneNumber: String,
        @Field("field") field: String,
        @Field("newFirstName") newFirstName: String
    ): Call<Void>

    @FormUrlEncoded
    @POST("UpdateVehicleDetails.php")
    fun updateVehicleDetails(
        @Field("id") id: Int,
        @Field("make") make: String,
        @Field("model") model: String,
        @Field("year") year: Int,
        @Field("type") type: String,
        @Field("licensePlateNo") licensePlateNo: String,
        @Field("insuranceCarrier") insuranceCarrier: String
    ): Call<String>


    //Delete
    @FormUrlEncoded
    @POST("DeleteUser.php")
    fun deleteUser(@Field("phoneNumber") phoneNumber: String): Call<String>

    @FormUrlEncoded
    @POST("DeleteVehicle.php")
    fun deleteVehicle(@Field("vehicleId") vehicleId: Int): Call<String>

}
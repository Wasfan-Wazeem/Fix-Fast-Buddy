package com.wasfan.fixfastbuddy


import com.wasfan.fixfastbuddy.dataClasses.CheckStatusDataClass
import com.wasfan.fixfastbuddy.dataClasses.DirectionsResponse
import com.wasfan.fixfastbuddy.dataClasses.FetchCancelledDataClass
import com.wasfan.fixfastbuddy.dataClasses.Vehicles
import com.wasfan.fixfastbuddy.dataClasses.fetchLocationDataClass
import com.wasfan.fixfastbuddy.dataClasses.requestDataClass
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

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

    //Requests
    @FormUrlEncoded
    @POST("ServiceRequest.php")
    fun submitServiceRequest(
        @Field("serviceID") serviceID: String,
        @Field("userPhoneNumber") userPhoneNumber: String,
        @Field("userLatitude") userLatitude: String,
        @Field("userLongitude") userLongitude: String,
        @Field("date") date: String,
        @Field("time") time: String,
        @Field("vehicleName") vehicleName: String,
        @Field("userAddress") userAddress: String?,
        @Field("vehicleId") vehicleId: String,
        @Field("description") description: String?
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("checkRequestStatus.php")
    fun checkRequestStatus(
        @Field("userPhoneNumber") userPhoneNumber: String
    ): Call<requestDataClass>

    @FormUrlEncoded
    @POST("CancelSearching.php")
    fun cancelSearching(
        @Field("userPhoneNumber") userPhoneNumber: String,
    ): Call<ResponseBody>

    //Get directions from google maps
    @GET("maps/api/directions/json")
    fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") apiKey: String
    ): Call<DirectionsResponse>

    @FormUrlEncoded
    @POST("FetchMechanicLocation.php")
    fun fetchMechanicLocation(
        @Field("mechanicPhoneNumber") mechanicPhoneNumber: String?
    ): Call<fetchLocationDataClass>

    @FormUrlEncoded
    @POST("FetchRequestDetails.php")
    fun fetchRequestDetails(
        @Field("requestId") requestId: String
    ): Call<requestDataClass>

    @GET("CheckServiceStatus.php")
    fun checkServiceStatus(
        @Query("requestId") requestId: String
    ): Call<CheckStatusDataClass>

    @GET("UpdateServiceStatus.php")
    fun updateServiceStatus(
        @Query("requestId") requestId: String,
        @Query("statusData") statusData: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("UpdateRatingAndReview.php")
    fun updateRatingAndReview(
        @Field("requestId") requestId: String,
        @Field("rating") rating: Float,
        @Field("review") review: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("FetchRequestDetailsRequestId.php")
    fun fetchRequestDetailsRequestId(
        @Field("requestId") requestId: String
    ): Call<requestDataClass>

    @FormUrlEncoded
    @POST("CancelServiceRequest.php")
    fun cancelServiceRequest(
        @Field("requestId") requestId: String,
        @Field("cancelledReason") cancelledReason: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("FetchCancelledRequests.php")
    fun fetchCancelledRequests(
        @Field("phoneNumber") phoneNumber: String,
    ): Call<List<FetchCancelledDataClass>>

    @FormUrlEncoded
    @POST("FetchCompletedRequests.php")
    fun fetchCompletedRequests(
        @Field("phoneNumber") phoneNumber: String,
    ): Call<List<FetchCancelledDataClass>>

    @FormUrlEncoded
    @POST("FetchOngoingRequest.php")
    fun fetchOngoingRequest(
        @Field("phoneNumber") phoneNumber: String,
    ): Call<List<FetchCancelledDataClass>>
}
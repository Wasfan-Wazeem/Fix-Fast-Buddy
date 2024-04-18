package com.wasfan.fixfastbuddy2.mechanic

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.wasfan.fixfastbuddy2.R
import com.wasfan.fixfastbuddy2.mechanic.DataClass.NearbyServiceRequestResponseDataClass
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.coroutines.*


class HomeFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        view.findViewById<Switch>(R.id.switch_online_offline)
            .setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    startLocationUpdates()

                } else {
                    stopLocationUpdates()
                }
            }
    }

    fun fetchNearbyServiceRequests(mechanicLat: Double, mechanicLng: Double, radius: Double) {
        val apiService = RetrofitInstance.api

        apiService.getNearbyServiceRequests(mechanicLat, mechanicLng, radius).enqueue(object : retrofit2.Callback<NearbyServiceRequestResponseDataClass> {
            override fun onResponse(call: retrofit2.Call<NearbyServiceRequestResponseDataClass>, response: retrofit2.Response<NearbyServiceRequestResponseDataClass>) {
                if (response.isSuccessful) {
                    response.body()?.let { nearbyRequestResponse ->
                        if (nearbyRequestResponse.success) {
                            // Handle the list of nearby requests
                            val nearbyRequests = nearbyRequestResponse.data
                            println(nearbyRequests)
                            // TODO: Update the UI or handle data as necessary
                        } else {
                            // Handle failure as per 'message' in response
                            print("Failed response")
                        }
                    }
                } else {
                    // Handle request error
                    println("Failed Request")
                }
            }

            override fun onFailure(call: retrofit2.Call<NearbyServiceRequestResponseDataClass>, t: Throwable) {
                // Handle network or other errors
            }
        })
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }

        val locationRequest = LocationRequest.create().apply {
            interval = 10000 // 5 seconds
            fastestInterval = 5000 // 3 seconds
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.forEach { location ->
                    var latitude = "${location?.latitude}"
                    var longitude = "${location?.longitude}"
                    val phoneNumber: String = "2147483647"
                    updateLocationToServer(phoneNumber, latitude, longitude)
                    fetchNearbyServiceRequests(location.latitude, location.longitude, 20.00)
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun updateLocationToServer(phoneNumber: String, latitude: String, longitude: String) {
        val retrofitAPI = RetrofitInstance.api

        val call: Call<ResponseBody> = retrofitAPI.updateMechanicLocation(phoneNumber, latitude, longitude)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseBody: ResponseBody? = response.body()
                    responseBody?.let {
                        val responseString = it.string()
                        // Parse the JSON responseString if needed
                        println(responseString)
                    }
                } else {
                    println("Failed to update location: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println("Failed to update location: ${t.message}")
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates()
        }
    }
}
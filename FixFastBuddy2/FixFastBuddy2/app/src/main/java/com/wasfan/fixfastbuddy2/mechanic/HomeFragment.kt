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
import okhttp3.ResponseBody


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
                    val latLng = LatLng(location.latitude, location.longitude)
                    updateLocationToServer(latLng)
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun updateLocationToServer(latLng: LatLng) {
        val apiService = RetrofitInstance.api

        val mechanicId = "2147483647" // Replace with the actual mechanic ID
        val abc = "newLocationsssss"

        apiService.updateMechanicLocation(mechanicId, abc)
            .enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: retrofit2.Call<ResponseBody>,
                    response: retrofit2.Response<ResponseBody>
                ) {
                    // Handle successful response
                    Toast.makeText(
                        requireContext(),
                        "Location Successfully Updated",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                    // Handle error
                    Toast.makeText(requireContext(), "Location Update Failed", Toast.LENGTH_SHORT).show()
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
package com.wasfan.fixfastbuddy

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.imageview.ShapeableImageView
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.google.android.gms.maps.model.Marker
import com.google.firebase.auth.FirebaseAuth
import com.wasfan.fixfastbuddy.dataClasses.ServiceResponseDataClass
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var backBtn: ImageButton
    private lateinit var confirmBtn: Button
    private lateinit var vehicleImageSIV: ShapeableImageView
    private lateinit var vehicleNameTV: TextView
    private lateinit var licensePlateNoTV: TextView
    private lateinit var serviceNameTV: TextView
    private lateinit var serviceImageSIV: ShapeableImageView
    private lateinit var currentLocationSBtn: ShapeableImageView

    private var mGoogleMap: GoogleMap? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private var lastLocationLatLng: LatLng? = null

    private var loadingDialog: Dialog? = null

    private val currentUser = FirebaseAuth.getInstance().currentUser
    val phoneNumber = currentUser?.phoneNumber.toString()

    companion object {
        private const val LOCATION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        init()
        lastLocationLatLng = LatLng(0.00,0.00)

        // Initialize FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        Places.initialize(applicationContext, getString(R.string.google_maps_api_key))
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG
            )
        )
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onError(p0: Status) {
                Toast.makeText(this@MapsActivity, "Some Error in Search", Toast.LENGTH_LONG).show()
            }

            override fun onPlaceSelected(place: Place) {
                lastLocationLatLng = place.latLng!!
                val addrr = place.address
                // val id = place.id
                autocompleteFragment.setText(addrr)
                val latLng = place.latLng!!
                zoomOnMap(latLng)
                addMarker(latLng)
            }
        })

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        backBtn.setOnClickListener {
            onBackPressed()
        }

        confirmBtn.setOnClickListener {
            if (lastLocationLatLng == null) {
                Toast.makeText(
                    this@MapsActivity,
                    "Please select a location.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val currentDate = Date()
                // For date
                val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                val formattedDate = dateFormat.format(currentDate)
                // For time
                val timeFormat = SimpleDateFormat("HH:mm:ss")
                val formattedTime = timeFormat.format(currentDate)

                var latitude = "${lastLocationLatLng?.latitude}"
                var longitude = "${lastLocationLatLng?.longitude}"

                sendServiceRequest("101", phoneNumber, latitude, longitude, formattedDate, formattedTime)

                showLoadingDialog()
                /*val intent = Intent(this, OngoingService::class.java)

                startActivity(intent)*/
            }
        }

        currentLocationSBtn.setOnClickListener {
            setUpMap()
        }

    }

    private fun showLoadingDialog() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.map_loading_box_layout, null)

        val cancelButton = view.findViewById<Button>(R.id.cancelButton)
        cancelButton.setOnClickListener {
            // Handle cancel button click
            cancelServiceRequest(phoneNumber)
            cancelLoadingDialog()
        }

        val loadingImageView = view.findViewById<ImageView>(R.id.loadingImage)
        // Load the rotation animation
        val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_animation)

        // Start the animation on the ImageView
        loadingImageView.startAnimation(rotateAnimation)

        loadingDialog = Dialog(this)
        loadingDialog?.setContentView(view)
        loadingDialog?.setCancelable(false) // To prevent cancel on outside touch
        loadingDialog?.show()
    }

    private fun cancelLoadingDialog() {
        loadingDialog?.dismiss()
        // Add your cancellation logic here
    }

    private fun sendServiceRequest(serviceID: String, userPhoneNumber: String, latitude: String, longitude: String, date: String, time: String) {
        val apiService = RetrofitInstance.api
        val call = apiService.submitServiceRequest(serviceID, userPhoneNumber, latitude, longitude, date, time)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // Handle successful response
                    val responseBody = response.body()?.string() // Get the raw JSON string
                    val message = "Success: $responseBody" // Use the raw JSON string as the message
                    Toast.makeText(this@MapsActivity, message, Toast.LENGTH_SHORT).show()
                    println(message)
                } else {
                    // Handle request errors depending on status code
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@MapsActivity, "Request Error: $errorBody", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Handle failure to execute the request, e.g., no internet, server down
                Toast.makeText(this@MapsActivity, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun cancelServiceRequest(userPhoneNumber: String) {
        val apiService = RetrofitInstance.api
        val call = apiService.cancelServiceRequest(userPhoneNumber)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // Handle successful response
                    val responseBody = response.body()?.string() // Get the raw JSON string
                    val message = "Success: $responseBody" // Use the raw JSON string as the message
                    Toast.makeText(this@MapsActivity, message, Toast.LENGTH_SHORT).show()
                    println(message)
                } else {
                    // Handle request errors depending on status code
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@MapsActivity, "Request Error: $errorBody", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Handle failure to execute the request, e.g., no internet, server down
                Toast.makeText(this@MapsActivity, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun init() {
        val vehicleName: String = intent.getStringExtra("vehicleName")!!
        val vehicleImage: Int = intent.getIntExtra("vehicleImage", 0)!!
        val serviceName: String = intent.getStringExtra("serviceName")!!
        val serviceImage: Int = intent.getIntExtra("serviceImage", 0)!!
        val licensePlateNo: String = intent.getStringExtra("licensePlateNo")!!

        backBtn = findViewById(R.id.back_button)
        confirmBtn = findViewById(R.id.confirmBtn)
        vehicleNameTV = findViewById(R.id.vehicleNameTV)
        vehicleImageSIV = findViewById(R.id.vehicleImageSIV)
        serviceNameTV = findViewById(R.id.serviceNameTV)
        serviceImageSIV = findViewById(R.id.serviceSIV)
        licensePlateNoTV = findViewById(R.id.vehicleLicensePLNoTV)
        currentLocationSBtn = findViewById(R.id.currentLocationSBtn)

        vehicleNameTV.text = vehicleName
        serviceNameTV.text = serviceName
        licensePlateNoTV.text = licensePlateNo
        vehicleImageSIV.setImageResource(vehicleImage)
        serviceImageSIV.setImageResource(serviceImage)


    }

    private fun addMarker(latLng: LatLng) {
        mGoogleMap?.clear()
        mGoogleMap?.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("Current Location")
        )
    }

    private fun zoomOnMap(latLng: LatLng) {
        val newLatLngZoom =
            CameraUpdateFactory.newLatLngZoom(latLng, 16f) //12f -> amount of zoom level
        mGoogleMap?.animateCamera(newLatLngZoom)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap

        mGoogleMap!!.uiSettings.isZoomControlsEnabled = true
        mGoogleMap!!.setOnMarkerClickListener(this)
        setUpMap()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
            return
        }
        mGoogleMap!!.isMyLocationEnabled = true
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLong = LatLng(location.latitude, location.longitude)
                lastLocationLatLng = currentLatLong
                addMarker(currentLatLong)
                zoomOnMap(currentLatLong)
            }
        }
    }

    override fun onMarkerClick(p0: Marker) = false

}
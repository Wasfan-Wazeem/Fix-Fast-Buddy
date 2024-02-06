package com.wasfan.fixfastbuddy

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import android.view.View
import com.google.android.gms.maps.model.Marker

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var backBtn: ImageButton
    private lateinit var confirmBtn: Button
    private lateinit var vehicleImageSIV: ShapeableImageView
    private lateinit var vehicleNameTV: TextView
    private lateinit var licensePlateNoTV: TextView
    private lateinit var serviceNameTV: TextView
    private lateinit var serviceImageSIV: ShapeableImageView
    private lateinit var currentLocationSBtn : ShapeableImageView

    private var mGoogleMap: GoogleMap? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var lastLocation : Location

    companion object{
        private const val LOCATION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        init()

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
            val intent = Intent(this, OngoingService::class.java)

            startActivity(intent)
        }

        currentLocationSBtn.setOnClickListener {
            setUpMap()
        }

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


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap

        mGoogleMap!!.uiSettings.isZoomControlsEnabled = true
        mGoogleMap!!.setOnMarkerClickListener(this)
        setUpMap()
    }

    private fun setUpMap(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
            return
        }
        mGoogleMap!!.isMyLocationEnabled = true
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(this) {location ->
            if(location!=null){
                lastLocation = location
                val currentLatLong = LatLng(location.latitude, location.longitude)
                addMarker(currentLatLong)
                zoomOnMap(currentLatLong)
            }
        }
    }

    override fun onMarkerClick(p0: Marker) = false

}
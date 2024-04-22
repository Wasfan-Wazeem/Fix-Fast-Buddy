package com.wasfan.fixfastbuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.maps.android.PolyUtil
import com.wasfan.fixfastbuddy.dataClasses.DirectionsResponse
import com.wasfan.fixfastbuddy.dataClasses.requestDataClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OngoingService : AppCompatActivity(), OnMapReadyCallback {

    private var mMap : GoogleMap? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var homeBtn : ImageButton
    private lateinit var nameTV: TextView
    private lateinit var garageNameTextView: TextView
    private lateinit var estimatedDurationTV: TextView
    private lateinit var estimatedDistanceTV: TextView
    private lateinit var vehicleNameTV: TextView
    private lateinit var vehicleLicensePlateNoTV: TextView
    private lateinit var serviceTV: TextView
    private lateinit var inspectionCostTV: TextView
    private lateinit var travellingCostTV: TextView
    private lateinit var totalCostTV: TextView

    private var requestData: requestDataClass? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ongoing_service)

        init()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.OngoingMapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val bottomSheetLayout : ConstraintLayout = findViewById(R.id.bottom_sheet_design)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)
        bottomSheetBehavior.isHideable = false
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        homeBtn.setOnClickListener {
            val intent = Intent(this, Navigation::class.java)
            startActivity(intent)
        }
    }

    private fun getDirections(origin: String, destination:String, apiKey: String){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(RetrofitAPI::class.java)

        val call = service.getDirections(origin, destination, apiKey)
        call.enqueue(object : Callback<DirectionsResponse> {
            override fun onResponse(call: Call<DirectionsResponse>, response: Response<DirectionsResponse>) {
                if (response.isSuccessful) {
                    // Here you can get the distance and duration:
                    val distance = response.body()?.routes?.firstOrNull()?.legs?.firstOrNull()?.distance?.text
                    val duration = response.body()?.routes?.firstOrNull()?.legs?.firstOrNull()?.duration?.text

                    // Now, you can update the UI with distance and duration, and also draw the route on the map
                    estimatedDistanceTV.text = distance
                    estimatedDurationTV.text = duration

                    val routePolyline = response.body()?.routes?.firstOrNull()?.overview_polyline?.points
                    routePolyline?.let {
                        val decodedPath = PolyUtil.decode(it)
                        val polylineOptions = PolylineOptions().addAll(decodedPath)
                        mMap?.addPolyline(polylineOptions)

                        // Adjusting the camera view to show the entire route
                        val boundsBuilder = LatLngBounds.Builder()
                        for (latLng in decodedPath) {
                            boundsBuilder.include(latLng)
                        }
                        val routeBounds = boundsBuilder.build()

                        // Convert 250dp to pixels for the bottom padding
                        val scale = resources.displayMetrics.density
                        val bottomPadding = (175 * scale + 0.5f).toInt() // 260dp to pixels

                        mMap?.setPadding(0, 0, 0, bottomPadding)

                        mMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(routeBounds, 300))
                    }
                }
            }

            override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }


    fun calculateTotal(inspectionCost: String?, travellingCost: String?): String {
        fun extractNumber(cost: String?): Double {
            return cost?.removePrefix("Rs")?.toDouble()!!
        }
        val total = extractNumber(inspectionCost) + extractNumber(travellingCost)
        return "Rs%.2f".format(total)
    }

    private fun init(){
        requestData = intent?.getParcelableExtra("requestData")
        homeBtn = findViewById(R.id.home_button)
        nameTV = findViewById(R.id.nameTV)
        garageNameTextView = findViewById(R.id.garageNameTV)
        estimatedDistanceTV = findViewById(R.id.estimatedDistanceTV)
        estimatedDurationTV = findViewById(R.id.estimatedDurationTV)
        vehicleNameTV = findViewById(R.id.vehicleName)
        vehicleLicensePlateNoTV = findViewById(R.id.vehicleLicensePlateNoTV)
        inspectionCostTV = findViewById(R.id.inspectionCostTV)
        travellingCostTV = findViewById(R.id.travellingCostTV)
        totalCostTV = findViewById(R.id.totalCostTV)

        nameTV.text = "${requestData?.firstName} ${requestData?.lastName}"
        vehicleNameTV.text = requestData?.vehicleName
        inspectionCostTV.text = requestData?.inspectionCost
        travellingCostTV.text = requestData?.travellingCost
        val totalCost = "Rs.1.00"
        totalCostTV.text = totalCost

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

// Enable zoom controls
        mMap?.uiSettings?.isZoomControlsEnabled = true
        // Enable zoom gestures (pinch to zoom)
        mMap?.uiSettings?.isZoomGesturesEnabled = true

        val origin = LatLng(requestData?.latitude?.toDouble()!!,requestData?.longitude?.toDouble()!!) // San Francisco
        //val origin = LatLng(6.94257,79.8619933)
        val originString = ("${origin.latitude}, ${origin.longitude}")
        addMarker(origin)

        val destination = LatLng(requestData?.userLatitude!!, requestData?.userLongitude!!) // San Francisco
        val destinationString = ("${destination.latitude}, ${destination.longitude}")
        addMarker(destination)
        val apiKey = getString(R.string.google_maps_api_key)

        getDirections(originString, destinationString, apiKey)
    }

    private fun addMarker(latLng: LatLng) {
        mMap?.addMarker(
            MarkerOptions()
                .position(latLng)
        )
    }
}
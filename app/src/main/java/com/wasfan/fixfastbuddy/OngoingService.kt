package com.wasfan.fixfastbuddy

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.maps.android.PolyUtil
import com.wasfan.fixfastbuddy.adapterClasses.CancellationReasonAdapter
import com.wasfan.fixfastbuddy.dataClasses.CheckStatusDataClass
import com.wasfan.fixfastbuddy.dataClasses.DirectionsResponse
import com.wasfan.fixfastbuddy.dataClasses.fetchLocationDataClass
import com.wasfan.fixfastbuddy.dataClasses.requestDataClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OngoingService : AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var homeBtn: ImageButton
    private lateinit var nameTV: TextView
    private lateinit var garageNameTextView: TextView
    private lateinit var estimatedDurationTV: TextView
    private lateinit var estimatedDistanceTV: TextView
    private lateinit var vehicleNameTV: TextView
    private lateinit var vehicleLicensePlateNoTV: TextView
    private lateinit var serviceTV: TextView
    private lateinit var descriptionTV: TextView
    private lateinit var problemTV: TextView
    private lateinit var serviceChargeTV: TextView
    private lateinit var inspectionCostTV: TextView
    private lateinit var travellingCostTV: TextView
    private lateinit var totalCostTV: TextView
    private lateinit var callBtn: Button
    private lateinit var textBtn: Button
    private lateinit var cancelBtn: Button
    private var currentMarker: Marker? = null

    private val currentUser = FirebaseAuth.getInstance().currentUser
    val phoneNumber = currentUser?.phoneNumber.toString()

    private lateinit var requestData: requestDataClass
    private lateinit var status: String
    var mechanicLatitude: Double = 0.00
    var mechanicLongitude: Double = 0.00
    private lateinit var serviceChargeData: CheckStatusDataClass
    private var runProcess: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ongoing_service)

        // Initialize UI components
        init()

        // Coroutine to fetch details before setting up the rest of the activity
        lifecycleScope.launch {
            fetchRequestDetails(Manager.onGoingRequestId!!)
        }
    }

    private fun setupMapAndBottomSheet() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.OngoingMapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val bottomSheetLayout: ConstraintLayout = findViewById(R.id.bottom_sheet_design)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)
        bottomSheetBehavior.isHideable = false
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        homeBtn.setOnClickListener {
            val intent = Intent(this, Navigation::class.java)
            startActivity(intent)
        }

        callBtn.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_DIAL, Uri.parse("tel:${requestData.mechanicPhoneNumber}"))
            startActivity(intent)
        }

        textBtn.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${requestData.mechanicPhoneNumber}"))
            startActivity(intent)
        }

        cancelBtn.setOnClickListener {
            showCancellingDialog()
        }

        process()
    }

    private fun process() {
        GlobalScope.launch {
            while (true) {
                checkServiceStatus(requestData.requestId)
                withContext(Dispatchers.Main) {
                    if (runProcess) {
                        when (status) {
                            "Arrived" -> {
                                val rootView = findViewById<View>(android.R.id.content)
                                Snackbar.make(
                                    rootView,
                                    "Mechanic has arrived at your location.",
                                    Snackbar.LENGTH_INDEFINITE
                                ).show()
                            }

                            "Service Charged" -> {
                                if (::serviceChargeData.isInitialized) { // Check if serviceChargeData has been initialized
                                    showChargesDialog()
                                    problemTV.text = serviceChargeData.problem
                                    serviceChargeTV.text = serviceChargeData.serviceCharge
                                }
                            }

                            "Cancelled" -> {
                                showMechanicCancelledDialog()
                            }

                            "Completed" -> {
                                Manager.onGoing = false
                                Manager.onGoingRequestId = null
                                val intent =
                                    Intent(this@OngoingService, RatingAndReview::class.java).apply {
                                        putExtra("requestId", requestData.requestId)
                                    }
                                startActivity(intent)
                                finish()
                            }
                        }
                        runProcess = false
                    }
                }
                if (status == "Completed") {
                    break
                }
                delay(3000) // Delay for 3 seconds before checking the status again
            }
        }
    }

    private fun showMechanicCancelledDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.mechanic_arrived_dialog)
        dialog.window!!.setBackgroundDrawable(getDrawable(R.drawable._10dp_border_radius_bg))
        dialog.setCanceledOnTouchOutside(false);

        // Set the text for problem and costs, you can change these to dynamic values
        dialog.findViewById<TextView>(R.id.ReasonTV).text =
            "Reason - ${serviceChargeData.cancelledReason}"

        // Set the listeners for the accept and decline buttons
        dialog.findViewById<Button>(R.id.homeButton).setOnClickListener {
            Manager.onGoing = false
            Manager.onGoingRequestId = null
            dialog.dismiss()
            startActivity(Intent(this@OngoingService, Navigation::class.java))
            finish()
        }
        dialog.show()
    }

    private fun showChargesDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.service_charge_popup_dialog)
        dialog.window!!.setBackgroundDrawable(getDrawable(R.drawable._10dp_border_radius_bg))
        dialog.setCanceledOnTouchOutside(false);

        // Set the text for problem and costs, you can change these to dynamic values
        dialog.findViewById<TextView>(R.id.problemTV).text = serviceChargeData.problem
        dialog.findViewById<TextView>(R.id.serviceTV).text = serviceChargeData.serviceId
        dialog.findViewById<TextView>(R.id.inspectionCostTV).text = "Rs.${serviceChargeData.inspectionCost}"
        dialog.findViewById<TextView>(R.id.travellingCostTV).text = "Rs.${serviceChargeData.travellingCost}"
        dialog.findViewById<TextView>(R.id.totalCostTV).text = "Rs.${serviceChargeData.totalCost}"

        // Set the listeners for the accept and decline buttons
        dialog.findViewById<Button>(R.id.acceptButton).setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                updateServiceStatus(requestData.requestId, "Proceed")
                withContext(Dispatchers.Main) {
                    dialog.dismiss()
                }
            }
        }
        dialog.findViewById<Button>(R.id.declineButton).setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                updateServiceStatus(requestData.requestId, "Decline")
                withContext(Dispatchers.Main) {
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    private fun showCancellingDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.cancelling_service_popup_dialog)
        dialog.window!!.setBackgroundDrawable(getDrawable(R.drawable.cancelling_dialog_bg))

        val layoutParams = dialog.window?.attributes
        layoutParams?.gravity = Gravity.BOTTOM
        layoutParams?.width = ViewGroup.LayoutParams.MATCH_PARENT // Set the width to Match content
        layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT // Set the height to wrap content
        dialog.window!!.attributes = layoutParams

        val reasonsList = listOf(
            "Mechanic too far away",
            "Too expensive service charge",
            "Have to take the vehicle to the garage to repair",
            "Mechanic asked to cancel",
            "Mechanic doesn't have the required tools",
            "Mechanic couldn't find the problem",
            "Other reasons"
        )

        val adapter = CancellationReasonAdapter(reasonsList) { position ->
            // Handle item click, position is the clicked item's position in the list
            var reason = reasonsList[position]
            requestData.cancelledReason = "User Cancelled : $reason"
        }
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.cancellationReasonsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        dialog.findViewById<ImageButton>(R.id.CloseButton).setOnClickListener {
            dialog.dismiss()
        }

        dialog.findViewById<Button>(R.id.confirmButton).setOnClickListener {
            if (adapter.lastCheckedPosition == -1) {
                Toast.makeText(this, "Please select a cancel reason.", Toast.LENGTH_LONG).show()
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    cancelServiceRequest(requestData.requestId, requestData.cancelledReason!!)
                }
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    private suspend fun cancelServiceRequest(requestId: String, cancelledReason: String) {
        val apiService = RetrofitInstance.api
        val call = apiService.cancelServiceRequest(requestId, cancelledReason)

        withContext(Dispatchers.IO) {
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {

                        Manager.onGoing = false
                        Manager.onGoingRequestId = null
                        startActivity(Intent(this@OngoingService, Navigation::class.java))
                        finish()

                    } else {
                        // Handle request errors depending on status code
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(
                            this@OngoingService,
                            "Request Error: $errorBody",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // Handle failure to execute the request, e.g., no internet, server down
                    Toast.makeText(this@OngoingService, "Failure: ${t.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        }
    }

    private suspend fun updateServiceStatus(requestId: String, statusData: String) {
        withContext(Dispatchers.IO) {
            val retrofitAPI = RetrofitInstance.api
            val call: Call<ResponseBody> = retrofitAPI.updateServiceStatus(requestId, statusData)

            try {
                val response: Response<ResponseBody> = call.execute()
                if (response.isSuccessful) {
                    val responseBody: ResponseBody? = response.body()
                    responseBody?.let {
                        println(responseBody)
                    }
                } else {
                    println("Failed to update location: ${response.message()}")
                }
            } catch (e: Exception) {
                println("Failed to update location: ${e.message}")
            }
        }
    }

    private suspend fun checkServiceStatus(requestId: String) {
        withContext(Dispatchers.IO) {
            val retrofitAPI = RetrofitInstance.api
            val call: Call<CheckStatusDataClass> = retrofitAPI.checkServiceStatus(requestId)

            try {
                val response: Response<CheckStatusDataClass> = call.execute()
                if (response.isSuccessful) {
                    val responseBody: CheckStatusDataClass? = response.body()
                    responseBody?.let {
                        status = it.status
                        println(status)
                        if (::serviceChargeData.isInitialized && status != serviceChargeData.status) {
                            runProcess = true
                            println(serviceChargeData.status)
                        }
                        println(runProcess)
                        serviceChargeData = it
                    }
                } else {
                    println("Failed to update location: ${response.message()}")
                }
            } catch (e: Exception) {
                println("Failed to update location: ${e.message}")
            }
        }
    }

    private suspend fun fetchMechanicLocation(mechanicPhoneNumber: String?) = coroutineScope {
        mechanicPhoneNumber?.let {
            val retrofitAPI = RetrofitInstance.api
            val call: Call<fetchLocationDataClass> = retrofitAPI.fetchMechanicLocation(it)

            val response = withContext(Dispatchers.IO) { call.execute() }
            if (response.isSuccessful) {
                response.body()?.let { location ->
                    mechanicLatitude = location.latitude
                    mechanicLongitude = location.longitude
                }
            } else {
                println("Failed to get location: ${response.message()}")
            }
        }
    }

    private fun getDirections(destination: String, origin: String) {

        val apiKey = getString(R.string.google_maps_api_key)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(RetrofitAPI::class.java)

        val call = service.getDirections(origin, destination, apiKey)
        call.enqueue(object : Callback<DirectionsResponse> {
            override fun onResponse(
                call: Call<DirectionsResponse>,
                response: Response<DirectionsResponse>
            ) {
                if (response.isSuccessful) {
                    // Here you can get the distance and duration:
                    val distance =
                        response.body()?.routes?.firstOrNull()?.legs?.firstOrNull()?.distance?.text
                    val duration =
                        response.body()?.routes?.firstOrNull()?.legs?.firstOrNull()?.duration?.text

                    // Now, you can update the UI with distance and duration, and also draw the route on the map
                    estimatedDistanceTV.text = distance
                    estimatedDurationTV.text = duration

                    val routePolyline =
                        response.body()?.routes?.firstOrNull()?.overview_polyline?.points
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

    private fun fetchRequestDetails(requestId: String) {
        val apiService = RetrofitInstance.api

        val call = apiService.fetchRequestDetails(requestId)
        call.enqueue(object : Callback<requestDataClass> {
            override fun onResponse(
                call: Call<requestDataClass>,
                response: Response<requestDataClass>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        requestData = data
                        status = requestData.status
                        setupMapAndBottomSheet()
                        updateUI()
                    } else {
                        println("Failed to fetch user details")
                        Toast.makeText(
                            this@OngoingService,
                            "Failed to fetch user details",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    println("Failed to fetch user details")
                    Toast.makeText(
                        this@OngoingService,
                        "Failed to fetch user details",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<requestDataClass>, t: Throwable) {
                println("Network error: ${t.message}")
                Toast.makeText(
                    this@OngoingService,
                    "Network error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun init() {
        homeBtn = findViewById(R.id.home_button)
        nameTV = findViewById(R.id.nameTV)
        garageNameTextView = findViewById(R.id.garageNameTV)
        estimatedDistanceTV = findViewById(R.id.estimatedDistanceTV)
        estimatedDurationTV = findViewById(R.id.estimatedDurationTV)
        vehicleNameTV = findViewById(R.id.vehicleName)
        vehicleLicensePlateNoTV = findViewById(R.id.vehicleLicensePlateNoTV)
        serviceTV = findViewById(R.id.serviceTV)
        descriptionTV= findViewById(R.id.descriptionTV)
        problemTV= findViewById(R.id.problemTV)
        serviceChargeTV= findViewById(R.id.serviceChargeTV)
        inspectionCostTV = findViewById(R.id.inspectionCostTV)
        travellingCostTV = findViewById(R.id.travellingCostTV)
        totalCostTV = findViewById(R.id.totalCostTV)
        callBtn = findViewById(R.id.callBtn)
        textBtn = findViewById(R.id.textBtn)
        cancelBtn = findViewById(R.id.cancelBtn)
    }

    private fun updateUI() {
        if (requestData.garageId == null) {
            garageNameTextView.text = "Self"
            nameTV.setTextSize(24f)
        }
        nameTV.text = "${requestData.firstName} ${requestData.lastName}"
        vehicleNameTV.text = requestData.vehicleName
        inspectionCostTV.text = "Rs.${requestData.inspectionCost}"
        travellingCostTV.text = "Rs.${requestData.travellingCost}"
        serviceTV.text = requestData.type
        if(requestData.description != null) {
            descriptionTV.text = requestData.description
        }
        val totalCost = requestData.inspectionCost + requestData.travellingCost!!
        totalCostTV.text = "Rs.$totalCost"
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.uiSettings?.isZoomControlsEnabled = true
        mMap?.uiSettings?.isZoomGesturesEnabled = true


        lifecycleScope.launch {
            val destination = LatLng(requestData.userLatitude, requestData.userLongitude)
            val destinationString = "${destination.latitude},${destination.longitude}"
            addMarker(destination, R.drawable.user_map_icon, 140, 140)
            var firstRun: Boolean = true

            while (isActive) {
                fetchMechanicLocation(requestData.mechanicPhoneNumber)
                currentMarker?.remove()
                val origin = LatLng(mechanicLatitude, mechanicLongitude)
                val originString = "${origin.latitude},${origin.longitude}"
                currentMarker = addMarker(origin, R.drawable.mechanic_map_icon, 140, 140)

                if (firstRun) {
                    getDirections(destinationString, originString)
                    firstRun = false
                }
                delay(4000L)
            }
        }
    }

    fun Bitmap.resize(width: Int, height: Int): Bitmap {
        return Bitmap.createScaledBitmap(this, width, height, false)
    }

    private fun addMarker(
        latLng: LatLng,
        @DrawableRes iconResId: Int,
        iconWidth: Int,
        iconHeight: Int
    ): Marker? {
        val bitmap =
            BitmapFactory.decodeResource(resources, iconResId).resize(iconWidth, iconHeight)
        return mMap?.addMarker(
            MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap)) // Use the resized bitmap here
        )
    }


}
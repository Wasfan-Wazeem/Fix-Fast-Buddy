package com.wasfan.fixfastbuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior

class OngoingService : AppCompatActivity(), OnMapReadyCallback {

    private var mGoogleMap : GoogleMap? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var homeBtn : ImageButton

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

    private fun init(){
        homeBtn = findViewById(R.id.home_button)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
    }
}
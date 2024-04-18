package com.wasfan.fixfastbuddy2.mechanic

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.wasfan.fixfastbuddy2.R
import com.wasfan.fixfastbuddy2.databinding.ActivityServiceProvidedBinding

class serviceProvided : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityServiceProvidedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_provided)

        val intent = Intent(this@serviceProvided, Navigation::class.java).apply{
            putExtra("fragment_to_show", "HomeFragment")
        }
        startActivity(intent)
    }
}

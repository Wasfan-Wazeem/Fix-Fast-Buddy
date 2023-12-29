package com.wasfan.fixfastbuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SignUpProfile : AppCompatActivity() {

    private lateinit var continueBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_profile)

        continueBtn = findViewById(R.id.signupContinueBtn)

        continueBtn.setOnClickListener {
            startActivity((Intent(this, SignUpVehicle::class.java)))
        }
    }
}
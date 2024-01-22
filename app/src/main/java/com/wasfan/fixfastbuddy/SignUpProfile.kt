package com.wasfan.fixfastbuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpProfile : AppCompatActivity() {

    private lateinit var firstNameET: EditText
    private lateinit var lastNameET: EditText
    private lateinit var emailET: EditText
    private lateinit var continueBtn: Button
    private lateinit var phoneNumber: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_profile)

        init()

        continueBtn.setOnClickListener {
            if (firstNameET.text.isEmpty() || lastNameET.text.isEmpty() || emailET.text.isEmpty()) {
                Toast.makeText(applicationContext, "Please enter all info to continue.", Toast.LENGTH_SHORT)
                    .show()
            }
            else {
                addUser()
            }
        }
    }

    private fun addUser() {
        val apiService = RetrofitInstance.api

        val fName = firstNameET.text.toString()
        val lName = lastNameET.text.toString()
        val email = emailET.text.toString()

        val call: Call<ResponseBody> = apiService.createUser(fName, lName, email, phoneNumber)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // Handle successful response
                    Toast.makeText(
                        applicationContext,
                        "User created successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@SignUpProfile, SignUpVehicle::class.java)
                    intent.putExtra("phoneNumber", phoneNumber)
                    startActivity(intent)
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(applicationContext, "Failed to create user", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Handle network failure
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun init() {
        firstNameET = findViewById(R.id.firstName)
        lastNameET = findViewById(R.id.lastName)
        emailET = findViewById(R.id.email)
        continueBtn = findViewById(R.id.signupContinueBtn)
        phoneNumber = intent.getStringExtra("phoneNumber")!!
    }
}
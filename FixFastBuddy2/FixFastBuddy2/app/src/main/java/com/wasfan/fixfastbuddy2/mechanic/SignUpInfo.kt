package com.wasfan.fixfastbuddy2.mechanic

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.wasfan.fixfastbuddy2.R
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date

class SignUpInfo : AppCompatActivity() {

    private lateinit var firstNameET: EditText
    private lateinit var lastNameET: EditText
    private lateinit var continueBtn: Button
    private lateinit var phoneNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_info)

        init()

        continueBtn.setOnClickListener {
            if (firstNameET.text.isEmpty() || lastNameET.text.isEmpty()) {
                Toast.makeText(applicationContext, "Please enter all info to continue.", Toast.LENGTH_SHORT)
                    .show()
            }
            else {
                addMechanic()
            }
        }

    }

    private fun init() {
        firstNameET = findViewById(R.id.firstName)
        lastNameET = findViewById(R.id.lastName)
        continueBtn = findViewById(R.id.signupContinueBtn)
        phoneNumber = "1234567890"
        //phoneNumber = intent.getStringExtra("phoneNumber")!!
    }

    private fun addMechanic() {
        val apiService = RetrofitInstance.api

        val fName = firstNameET.text.toString()
        val lName = lastNameET.text.toString()
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val formattedDate = dateFormat.format(currentDate)

        val call: Call<ResponseBody> = apiService.createMechanic(fName, lName, phoneNumber, formattedDate)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful && response.body() != null) {
                    // Handle successful response
                    Toast.makeText(
                        applicationContext,
                        "User created successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@SignUpInfo, serviceProvided::class.java)
                    intent.putExtra("phoneNumber", phoneNumber)
                    startActivity(intent)
                    finish()
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

}
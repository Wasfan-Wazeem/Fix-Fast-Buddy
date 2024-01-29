package com.wasfan.fixfastbuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddNewVehicle : AppCompatActivity() {

    private lateinit var makeSpinner: Spinner
    private lateinit var modelSpinner: Spinner
    private lateinit var yearSpinner: Spinner
    private lateinit var typeSpinner: Spinner
    private lateinit var insuranceCarrierSpinner: Spinner
    private lateinit var licensePlate : EditText
    private lateinit var doneBtn : Button

    private val currentUser = FirebaseAuth.getInstance().currentUser
    val phoneNumber = currentUser?.phoneNumber.toString()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_vehicle)

        makeSpinner = findViewById(R.id.makeSpinner)
        modelSpinner = findViewById(R.id.modelSpinner)
        yearSpinner = findViewById((R.id.yearSpinner))
        typeSpinner = findViewById(R.id.typeSpinner)
        insuranceCarrierSpinner = findViewById((R.id.insuranceCarrierSpinner))
        licensePlate = findViewById((R.id.licensePlateNo))
        doneBtn = findViewById(R.id.signupDoneBtn)



        spinner(makeSpinner,FreqUsed.Makes)
        spinner(modelSpinner,FreqUsed.Models)
        spinner(yearSpinner,FreqUsed.Years)
        spinner(typeSpinner,FreqUsed.Types)
        spinner(insuranceCarrierSpinner,FreqUsed.Insurances)

        doneBtn.setOnClickListener {
            if (makeSpinner.selectedItem == 0 || modelSpinner.selectedItem == 0 || yearSpinner.selectedItem == 0
                || typeSpinner.selectedItem == 0 || insuranceCarrierSpinner.selectedItem == 0 || licensePlate.text.isEmpty()) {
                Toast.makeText(applicationContext, "Please enter all info to continue.", Toast.LENGTH_SHORT)
                    .show()
            }
            else {
                addVehicle()
            }
        }

    }

    private fun spinner(spinner: Spinner, itemsList: List<String>){

        val arrayAdapterMake =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, itemsList)
        arrayAdapterMake.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapterMake
        spinner.setSelection(0)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    // Handle the hint selection (optional)
                } else {
                    val selectedItem = parent?.getItemAtPosition(position)?.toString()
                    Toast.makeText(
                        this@AddNewVehicle,
                        "Your car Make is $selectedItem",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun addVehicle() {
        val apiService = RetrofitInstance.api

        val make: String = makeSpinner.selectedItem.toString()
        val model: String = modelSpinner.selectedItem.toString()
        val year: String = yearSpinner.selectedItem.toString()
        val type: String = typeSpinner.selectedItem.toString()
        val insurance : String = insuranceCarrierSpinner.selectedItem.toString()
        val licenseNo : String = licensePlate.text.toString()

        val call: Call<ResponseBody> = apiService.createVehicle(make, model, year, type, licenseNo, insurance, phoneNumber)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // Handle successful response
                    Toast.makeText(
                        applicationContext,
                        "Vehicle created successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity((Intent(this@AddNewVehicle, Navigation::class.java)))
                    finish()
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(applicationContext, "Failed to create vehicle", Toast.LENGTH_SHORT)
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
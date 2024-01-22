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
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpVehicle : AppCompatActivity() {

    private lateinit var makeSpinner: Spinner
    private lateinit var modelSpinner: Spinner
    private lateinit var yearSpinner: Spinner
    private lateinit var typeSpinner: Spinner
    private lateinit var insuranceCarrierSpinner: Spinner
    private lateinit var licensePlate : EditText
    private lateinit var doneBtn : Button
    private lateinit var phoneNumber : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_vehicle)

        makeSpinner = findViewById(R.id.makeSpinner)
        modelSpinner = findViewById(R.id.modelSpinner)
        yearSpinner = findViewById((R.id.yearSpinner))
        typeSpinner = findViewById(R.id.typeSpinner)
        insuranceCarrierSpinner = findViewById((R.id.insuranceCarrierSpinner))
        licensePlate = findViewById((R.id.licensePlateNo))
        doneBtn = findViewById(R.id.signupDoneBtn)
        phoneNumber = intent.getStringExtra("phoneNumber")!!

        spinners()

        doneBtn.setOnClickListener {
            addVehicle()
        }

    }

    private fun spinners() {

        //MAKE SPINNER
        val listItemsMake = listOf("Make", "Toyota", "Honda", "Mercedes Benz", "BMW", "Porsche")

        val arrayAdapterMake =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, listItemsMake)
        arrayAdapterMake.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        makeSpinner.adapter = arrayAdapterMake
        makeSpinner.setSelection(0)

        makeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                        this@SignUpVehicle,
                        "Your car Make is $selectedItem",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }


        //MODEL SPINNER
        val listItemsModel = listOf("Model", "318i", "Civic", "C200", "Accord", "Panamera")

        val arrayAdapterModel =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, listItemsModel)
        arrayAdapterModel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        modelSpinner.adapter = arrayAdapterModel
        modelSpinner.setSelection(0)

        modelSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                        this@SignUpVehicle,
                        "Your car Make is $selectedItem",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }


        //YEAR SPINNER
        val listItemsYear = listOf("Year", "2007", "2011", "2017", "2020", "2022")

        val arrayAdapterYear =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, listItemsYear)
        arrayAdapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearSpinner.adapter = arrayAdapterYear
        yearSpinner.setSelection(0)

        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                        this@SignUpVehicle,
                        "Your car Make is $selectedItem",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }


        //Type SPINNER
        val listItemsType = listOf("Type", "Car", "Van", "Bike", "Bus", "Lorry")

        val arrayAdapterType =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, listItemsType)
        arrayAdapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        typeSpinner.adapter = arrayAdapterType
        typeSpinner.setSelection(0)

        typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                        this@SignUpVehicle,
                        "Your car Make is $selectedItem",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }


        //Insurance Carrier SPINNER
        val listItemsInsurances = listOf(
            "Insurance Carrier",
            "Ceylinco Insurance",
            "Sri Lanka Insurance",
            "Allianz Insurance",
            "FairFirst Insurance",
            "NTB Insurance"
        )

        val arrayAdapterInsurance =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, listItemsInsurances)
        arrayAdapterInsurance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        insuranceCarrierSpinner.adapter = arrayAdapterInsurance
        insuranceCarrierSpinner.setSelection(0)

        insuranceCarrierSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
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
                            this@SignUpVehicle,
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
                    startActivity((Intent(this@SignUpVehicle, Navigation::class.java)))
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
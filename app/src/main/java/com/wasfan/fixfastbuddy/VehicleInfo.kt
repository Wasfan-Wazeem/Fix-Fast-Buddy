package com.wasfan.fixfastbuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast


class VehicleInfo : AppCompatActivity() {

    private lateinit var vehicleName: String
    private lateinit var vehicleImage: ImageView
    private lateinit var vehicleText: TextView
    private lateinit var makeSpinner: Spinner
    private lateinit var imagePickerBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_info)

        val imageResource = intent.getIntExtra("image", 0)
        init()
        makeSpinner()

        vehicleText.text = vehicleName
        vehicleImage.setImageResource(imageResource)

        imagePickerBtn.setOnClickListener {
            pickImage()
        }


        val items = listOf("Item 1", "Item 2", "Item 3")

        val customSpinner = findViewById<Spinner>(R.id.makeSpinner)
        val adapter = CustomSpinnerAdapter(this, R.layout.vehicle_info_spinner_item, items)
        customSpinner.adapter = adapter

        customSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = items[position]
                Toast.makeText(
                    this@VehicleInfo,
                    "Selected Item: $selectedItem",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing
            }
        }



        

    }

    private fun init() {
        vehicleImage = findViewById(R.id.vehicleInfoImage)
        vehicleText = findViewById(R.id.vehicleInfoText)
        makeSpinner = findViewById(R.id.makeSpinner)
        imagePickerBtn = findViewById(R.id.vehicleImagePicker)

        vehicleName = intent.getStringExtra("vehicleName").toString()
    }

    private fun makeSpinner() {
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
                        this@VehicleInfo,
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

    //ImagePicker
    fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 101) {
            val uri = data?.data
            vehicleImage.setImageURI(uri)
        }
    }
    //ImagePickerEnd

}
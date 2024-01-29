package com.wasfan.fixfastbuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.FieldPosition
import kotlin.properties.Delegates


class VehicleInfo : AppCompatActivity() {

    private lateinit var vehicleImage: ImageView
    private lateinit var vehicleText: TextView
    private lateinit var makeSpinner: Spinner
    private lateinit var modelSpinner: Spinner
    private lateinit var yearSpinner: Spinner
    private lateinit var typeSpinner: Spinner
    private lateinit var insuranceSpinner: Spinner
    private lateinit var imagePickerBtn: ImageButton
    private lateinit var closeBtn: ImageButton
    private lateinit var saveChangesBtn: Button
    private lateinit var deleteVehicleBtn: Button
    private lateinit var licensePlateNoET: EditText

    private val currentUser = FirebaseAuth.getInstance().currentUser
    val phoneNumber = currentUser?.phoneNumber.toString()

    private val listItemsMake = FreqUsed.Makes
    private val listItemsModel = FreqUsed.Models
    private val listItemsYear = FreqUsed.Years
    private val listItemsType = FreqUsed.Types
    private val listItemsInsuranceCarrier = FreqUsed.Insurances
    private var vehicleId by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_info)

        val imageResource = intent.getIntExtra("image", 0)
        init()
        getVehicleInfo()

        vehicleImage.setImageResource(imageResource)

        imagePickerBtn.setOnClickListener {
            pickImage()
        }

        spinner(makeSpinner,listItemsMake)
        spinner(modelSpinner,listItemsModel)
        spinner(yearSpinner,listItemsYear)
        spinner(typeSpinner,listItemsType)
        spinner(insuranceSpinner,listItemsInsuranceCarrier)

        closeBtn.setOnClickListener {
            onBackPressed()
        }

        saveChangesBtn.setOnClickListener {
            showSaveConfirmationDialog()
        }

        deleteVehicleBtn.setOnClickListener {
            showDeleteConfirmationDialog()
        }

    }

    private fun init() {
        vehicleImage = findViewById(R.id.vehicleInfoImage)
        vehicleText = findViewById(R.id.vehicleInfoText)
        makeSpinner = findViewById(R.id.makeSpinner)
        modelSpinner = findViewById(R.id.modelSpinner)
        yearSpinner = findViewById(R.id.yearSpinner)
        typeSpinner = findViewById(R.id.typeSpinner)
        insuranceSpinner = findViewById(R.id.insuranceCarrierSpinner)
        licensePlateNoET = findViewById(R.id.licensePlateNoET)
        imagePickerBtn = findViewById(R.id.vehicleImagePicker)
        closeBtn = findViewById(R.id.vehicleInfoClose)
        saveChangesBtn = findViewById(R.id.saveChangesBtn)
        deleteVehicleBtn = findViewById(R.id.deleteVehicleBtn)
    }

    private fun spinner(spinner: Spinner, itemsList: List<String>){

        val arrayAdapterMake =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, itemsList)
        arrayAdapterMake.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapterMake
        spinner.setSelection(0)

    }

    private fun getVehicleInfo(){
        val position: Int = intent.getIntExtra("position", 0)

        val apiService = RetrofitInstance.api

        val call: Call<List<Vehicles>> = apiService.getVehicleDetails(phoneNumber)

        call.enqueue(object : Callback<List<Vehicles>> {
            override fun onResponse(call: Call<List<Vehicles>>, response: Response<List<Vehicles>>) {
                if (response.isSuccessful) {
                    val vehicleListDB: List<Vehicles>? = response.body()
                    if (vehicleListDB != null) {
                        val vehicle = vehicleListDB[position]
                        vehicleText.text = "${vehicle.make} ${vehicle.model} ${vehicle.year}"

                        setSpinner(vehicle.make, listItemsMake, makeSpinner)
                        setSpinner(vehicle.model,listItemsModel, modelSpinner)
                        setSpinner(vehicle.year, listItemsYear, yearSpinner)
                        setSpinner(vehicle.type, listItemsType, typeSpinner)
                        setSpinner(vehicle.insuranceCarrier, listItemsInsuranceCarrier, insuranceSpinner)
                        licensePlateNoET.setText(vehicle.licensePlateNo)

                        vehicleId = vehicle.vehicleId
                    }


                } else {
                    // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<List<Vehicles>>, t: Throwable) {
                // Handle network error
            }
        })
    }

    private fun setSpinner(text: String, list: List<String>, spinner: Spinner) {
        val textToSet = text
        val positionToSet = list.indexOf(textToSet)
        if (positionToSet != -1) {
            spinner.setSelection(positionToSet)
        }
    }

    private fun updateDetails(id: Int, make: String, model: String, year: Int, type: String, licensePlateNo: String, insuranceCarrier: String) {
        val apiService = RetrofitInstance.api

        val call = apiService.updateVehicleDetails(id, make, model, year, type, licensePlateNo, insuranceCarrier)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val intent = Intent(this@VehicleInfo, Navigation::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    // Handle failed update
                    Toast.makeText(this@VehicleInfo, "Failed to update Vehicle", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                // Handle network error
                Toast.makeText(this@VehicleInfo, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteVehicleDetails(){
        val apiService = RetrofitInstance.api

        val call = apiService.deleteVehicle(vehicleId)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {

                } else {
                    println("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                println("Network request failed with exception: ${t.message}")
            }
        })
    }

    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.delete_popup_layout, null)

        dialogView.findViewById<TextView>(R.id.dialogTitle).text = "Delete Vehicle"
        dialogView.findViewById<TextView>(R.id.dialogMessage).text = "Are you sure you want to delete this vehicle?"

        builder.setView(dialogView)
        val alertDialog = builder.create()

        // Set the cancel button action
        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            alertDialog.dismiss()
        }

        // Set the delete button action
        dialogView.findViewById<Button>(R.id.btnDelete).setOnClickListener {
            deleteVehicleDetails()
            val intent = Intent(this@VehicleInfo, Navigation::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun showSaveConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.delete_popup_layout, null)

        dialogView.findViewById<TextView>(R.id.dialogTitle).text = "Save Changes"
        dialogView.findViewById<TextView>(R.id.dialogMessage).text = "Are you sure you want to save changes?"
        dialogView.findViewById<Button>(R.id.btnDelete).text = "Update"
        dialogView.findViewById<Button>(R.id.btnDelete).backgroundTintList = ContextCompat.getColorStateList(this, R.color.lightgreen)


        builder.setView(dialogView)
        val alertDialog = builder.create()


        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            alertDialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnDelete).setOnClickListener {
            updateDetails(vehicleId , makeSpinner.selectedItem.toString(), modelSpinner.selectedItem.toString(),yearSpinner.selectedItem.toString().toInt(),typeSpinner.selectedItem.toString(), licensePlateNoET.text.toString() , insuranceSpinner.selectedItem.toString())
            alertDialog.dismiss()
        }

        alertDialog.show()
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
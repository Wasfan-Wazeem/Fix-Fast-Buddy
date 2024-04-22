package com.wasfan.fixfastbuddy

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.wasfan.fixfastbuddy.dataClasses.UploadResponse
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ViewProfile : AppCompatActivity(), UploadRequestBody.UploadCallback {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<ViewProfileDataClass>
    private lateinit var titleList : Array<String>
    private lateinit var infoList : Array<String>
    private lateinit var deleteBtn : Button
    private lateinit var backBtn : ImageButton
    private lateinit var imagePickerBtn: ImageButton
    private lateinit var profilePic: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutRoot: ConstraintLayout
    private var selectedImageUri: Uri? = null


    private lateinit var auth : FirebaseAuth
    private val currentUser = FirebaseAuth.getInstance().currentUser
    val pNumber = currentUser?.phoneNumber.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)

        auth = FirebaseAuth.getInstance()
        deleteBtn = findViewById(R.id.deleteBtn)
        backBtn = findViewById(R.id.backBtn)
        imagePickerBtn = findViewById(R.id.profileImagePicker)
        profilePic = findViewById(R.id.profilePicIV)
        layoutRoot = findViewById(R.id.layout_root)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.INVISIBLE

        fetchUserDetails(pNumber)

        imagePickerBtn.setOnClickListener {
            imagePicker()
            uploadImage()
        }

        backBtn.setOnClickListener {
            onBackPressed()
        }

        deleteBtn.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    //Image Scene

    private fun uploadImage() {

        if (selectedImageUri == null) {
            layoutRoot.snackbar("Select an Image First")
            return
        }

        val parcelFileDescriptor = contentResolver.openFileDescriptor(
            selectedImageUri!!, "r", null
        ) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(cacheDir,contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        progressBar.progress = 0
        val body = UploadRequestBody(file,"image",this)


        MyApi().uploadImage(MultipartBody.Part.createFormData(
            "image",
            file.name,
            body
        ),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),"json")
        ).enqueue(object : Callback<UploadResponse>{
            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                response.body()?.let {
                    layoutRoot.snackbar(it.message)
                    progressBar.progress = 100
                    progressBar.visibility = View.INVISIBLE
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                layoutRoot.snackbar(t.message!!)
                progressBar.progress = 0
            }

        })
    }

    private fun imagePicker() {

        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_IMAGE -> {
                    selectedImageUri = data?.data
                    profilePic.setImageURI(selectedImageUri)
                    uploadImage()
                }
            }
        }
    }

    override fun onProgressUpdate(percentage: Int) {
        progressBar.progress = percentage
    }

    private fun ContentResolver.getFileName(selectedImageUri: Uri): String {
        var name = ""
        val returnCursor = this.query(selectedImageUri,null,null,null,null)
        if (returnCursor!=null){
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }

        return name
    }

    private fun View.snackbar(message: String) {
        Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
            snackbar.setAction("OK") {
                snackbar.dismiss()
            }
        }.show()

    }

    //Close Image Scene

    private fun fetchUserDetails(pNumber: String) {
        val apiService = RetrofitInstance.api

        val call = apiService.getUserDetails(pNumber)
        call.enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        // Process the user details
                        infoList = arrayOf(
                            "${user.firstName}",
                            "${user.lastName}",
                            "${user.phoneNumber}",
                            "${user.email}",
                            "${user.dateJoined}",
                            "07",
                            "05",
                            "02",
                            "02"
                        )

                        // Initialize RecyclerView after receiving user details
                        recyclerView()
                    } else {
                        Toast.makeText(this@ViewProfile, "Failed to fetch user details", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ViewProfile, "Failed to fetch user details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                Toast.makeText(this@ViewProfile, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun recyclerView(){

        titleList = arrayOf(
            "First Name",
            "Last Name",
            "Mobile Number",
            "Email",
            "Date joined",
            "Total services requested",
            "Services completed",
            "Services cancelled",
            "Total bookings"
        )

        recyclerView = findViewById(R.id.viewProfileRW)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        dataList = arrayListOf<ViewProfileDataClass>()

        for(i in titleList.indices){
            val dataClass = ViewProfileDataClass(titleList[i],infoList[i])
            dataList.add(dataClass)
        }
        var adapter = ViewProfileAdapterClass(dataList)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : ViewProfileAdapterClass.onItemClickListener{
            override fun onItemClick(position: Int, text: String) {
                if(position<4) {
                    showPopup(text)
                }
            }
        })

    }

    private fun showPopup(text: String) {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.view_profile_popup_layout, null)

        val infoTextView: TextView = view.findViewById(R.id.infoTextView)
        val editTextInput: EditText = view.findViewById(R.id.editTextInput)
        val saveButton: Button = view.findViewById(R.id.saveButton)
        val cancelButton: Button = view.findViewById(R.id.cancelButton)

        infoTextView.text = "Enter Your $text"

        // Add click listeners for the buttons
        saveButton.setOnClickListener {
            // Handle Save button click
            val inputText = editTextInput.text.toString()
            var field=""
            field = when (text) {
                "First Name" -> "firstName"
                "Last Name" -> "lastName"
                "Mobile Number" -> "phoneNumber"
                "Email" -> "email"
                else -> ""}
            updateDetails(pNumber, field, inputText)
            alertDialog.dismiss()
        }

        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }

        builder.setView(view)
        alertDialog = builder.create()

        // Show the dialog
        alertDialog.show()
    }

    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.delete_popup_layout, null)

        builder.setView(dialogView)
        val alertDialog = builder.create()

        // Set the cancel button action
        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            alertDialog.dismiss()
        }

        // Set the delete button action
        dialogView.findViewById<Button>(R.id.btnDelete).setOnClickListener {
            deleteUserDetails(pNumber)
            val intent = Intent(this@ViewProfile, Phone::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    companion object {
        lateinit var alertDialog: AlertDialog
        const val REQUEST_CODE_IMAGE = 101
    }

    private fun updateDetails(phoneNumber: String, field: String, newFirstName: String) {
        val apiService = RetrofitInstance.api

        val call = apiService.updateUserDetails(phoneNumber, field, newFirstName)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    fetchUserDetails(pNumber)
                } else {
                    // Handle failed update
                    Toast.makeText(this@ViewProfile, "Failed to update $field", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Handle network error
                Toast.makeText(this@ViewProfile, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteUserDetails(phoneNumber: String){
        val apiService = RetrofitInstance.api

        val call = apiService.deleteUser(phoneNumber)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    //handle successful response
                } else {
                    println("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                println("Network request failed with exception: ${t.message}")
            }
        })
    }
}


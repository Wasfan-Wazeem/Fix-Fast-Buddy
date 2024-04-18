package com.wasfan.fixfastbuddy2.mechanic

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wasfan.fixfastbuddy2.R
import com.wasfan.fixfastbuddy2.mechanic.AdapterClass.ViewProfileAdapterClass
import com.wasfan.fixfastbuddy2.mechanic.DataClass.ViewProfileDataClass



class ViewProfile : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<ViewProfileDataClass>
    private lateinit var titleList : Array<String>
    private lateinit var infoList : Array<String>
//    private lateinit var deleteBtn : Button
    private lateinit var backBtn2 : ImageButton
//    private lateinit var imagePickerBtn: ImageButton
//    private lateinit var profilePic: ImageView


//    private lateinit var auth : FirebaseAuth
//    private val currentUser = FirebaseAuth.getInstance().currentUser
//    val pNumber = currentUser?.phoneNumber.toString()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_view_profile)
//
//        auth = FirebaseAuth.getInstance()
//        deleteBtn = findViewById(R.id.deleteBtn)
//        backBtn = findViewById(R.id.backBtn)
//        imagePickerBtn = findViewById(R.id.profileImagePicker)
//        profilePic = findViewById(R.id.profilePicIV)
//
//        fetchUserDetails(pNumber)
//
//        imagePickerBtn.setOnClickListener {
//            pickImage()
//        }
//
//        backBtn.setOnClickListener {
//            onBackPressed()
//        }
//
//        deleteBtn.setOnClickListener {
//            showDeleteConfirmationDialog()
//        }
//    }
//
//    //ImagePicker
//    fun pickImage() {
//        val intent = Intent(Intent.ACTION_PICK)
//        intent.type = "image/*"
//        startActivityForResult(intent, 102)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == RESULT_OK && requestCode == 102) {
//            val uri = data?.data
//            // Assuming 'profilePic' is the ImageView where you want to display the selected image
//            profilePic.setImageURI(uri)
//            // If you want to upload the selected image to the server, you can call a function here
//            // and pass the 'uri' to that function.
//        }
//    }
//    //ImagePickerEnd
//
//
//    private fun fetchUserDetails(pNumber: String) {
//        val apiService = RetrofitInstance.api
//
//        val call = apiService.getUserDetails(pNumber)
//        call.enqueue(object : Callback<Users> {
//            override fun onResponse(call: Call<Users>, response: Response<Users>) {
//                if (response.isSuccessful) {
//                    val user = response.body()
//                    if (user != null) {
//                        // Process the user details
//                        infoList = arrayOf(
//                            "${user.firstName}",
//                            "${user.lastName}",
//                            "${user.phoneNumber}",
//                            "${user.email}",
//                            "${user.dateJoined}",
//                            "07",
//                            "05",
//                            "02",
//                            "02"
//                        )
//
//                        // Initialize RecyclerView after receiving user details
//                        recyclerView()
//                    } else {
//                        Toast.makeText(this@ViewProfile, "Failed to fetch user details", Toast.LENGTH_SHORT).show()
//                    }
//                } else {
//                    Toast.makeText(this@ViewProfile, "Failed to fetch user details", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<Users>, t: Throwable) {
//                Toast.makeText(this@ViewProfile, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//    private fun recyclerView(){
//
//        titleList = arrayOf(
//            "First Name",
//            "Last Name",
//            "Mobile Number",
//            "Email",
//            "Date joined",
//            "Total services requested",
//            "Services completed",
//            "Services cancelled",
//            "Total bookings"
//        )
//
//        recyclerView = findViewById(R.id.viewProfileRW)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.setHasFixedSize(true)
//
//        dataList = arrayListOf<ViewProfileDataClass>()
//
//        for(i in titleList.indices){
//            val dataClass = ViewProfileDataClass(titleList[i],infoList[i])
//            dataList.add(dataClass)
//        }
//        var adapter = ViewProfileAdapterClass(dataList)
//        recyclerView.adapter = adapter
//        adapter.setOnItemClickListener(object : ViewProfileAdapterClass.onItemClickListener{
//            override fun onItemClick(position: Int, text: String) {
//                if(position<4) {
//                    showPopup(text)
//                }
//            }
//        })
//
//    }
//
//    private fun showPopup(text: String) {
//        val builder = AlertDialog.Builder(this)
//        val inflater = LayoutInflater.from(this)
//        val view = inflater.inflate(R.layout.view_profile_popup_layout, null)
//
//        val infoTextView: TextView = view.findViewById(R.id.infoTextView)
//        val editTextInput: EditText = view.findViewById(R.id.editTextInput)
//        val saveButton: Button = view.findViewById(R.id.saveButton)
//        val cancelButton: Button = view.findViewById(R.id.cancelButton)
//
//        infoTextView.text = "Enter Your $text"
//
//        // Add click listeners for the buttons
//        saveButton.setOnClickListener {
//            // Handle Save button click
//            val inputText = editTextInput.text.toString()
//            var field=""
//            field = when (text) {
//                "First Name" -> "firstName"
//                "Last Name" -> "lastName"
//                "Mobile Number" -> "phoneNumber"
//                "Email" -> "email"
//                else -> ""}
//            updateDetails(pNumber, field, inputText)
//            alertDialog.dismiss()
//        }
//
//        cancelButton.setOnClickListener {
//            alertDialog.dismiss()
//        }
//
//        builder.setView(view)
//        alertDialog = builder.create()
//
//        // Show the dialog
//        alertDialog.show()
//    }
//
//    private fun showDeleteConfirmationDialog() {
//        val builder = AlertDialog.Builder(this)
//        val inflater = layoutInflater
//        val dialogView = inflater.inflate(R.layout.delete_popup_layout, null)
//
//        builder.setView(dialogView)
//        val alertDialog = builder.create()
//
//        // Set the cancel button action
//        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
//            alertDialog.dismiss()
//        }
//
//        // Set the delete button action
//        dialogView.findViewById<Button>(R.id.btnDelete).setOnClickListener {
//            deleteUserDetails(pNumber)
//            val intent = Intent(this@ViewProfile, Phone::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            startActivity(intent)
//            finish()
//            alertDialog.dismiss()
//        }
//
//        alertDialog.show()
//    }
//
//    companion object {
//        lateinit var alertDialog: AlertDialog
//    }
//
//    private fun updateDetails(phoneNumber: String, field: String, newFirstName: String) {
//        val apiService = RetrofitInstance.api
//
//        val call = apiService.updateUserDetails(phoneNumber, field, newFirstName)
//        call.enqueue(object : Callback<Void> {
//            override fun onResponse(call: Call<Void>, response: Response<Void>) {
//                if (response.isSuccessful) {
//                    fetchUserDetails(pNumber)
//                } else {
//                    // Handle failed update
//                    Toast.makeText(this@ViewProfile, "Failed to update $field", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<Void>, t: Throwable) {
//                // Handle network error
//                Toast.makeText(this@ViewProfile, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//    private fun deleteUserDetails(phoneNumber: String){
//        val apiService = RetrofitInstance.api
//
//        val call = apiService.deleteUser(phoneNumber)
//
//        call.enqueue(object : Callback<String> {
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                if (response.isSuccessful) {
//                    //handle successful response
//                } else {
//                    println("Error: ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                println("Network request failed with exception: ${t.message}")
//            }
//        })
//    }
//}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)

    backBtn2 = findViewById(R.id.backBtn2)
    backBtn2.setOnClickListener {
        onBackPressed()}

//    deleteBtn = findViewById(R.id.deleteBtn)
//        backBtn = findViewById(R.id.backBtn)
//        imagePickerBtn = findViewById(R.id.profileImagePicker)
//        profilePic = findViewById(R.id.profilePicIV)
//
//        imagePickerBtn.setOnClickListener {
//            pickImage()
//        }
//
//        backBtn.setOnClickListener {
//            onBackPressed()
//        }
//
//        deleteBtn.setOnClickListener {
//            showDeleteConfirmationDialog()
//        }}

//    private fun showDeleteConfirmationDialog() {
//        TODO("Not yet implemented")
//    }
//
//    //ImagePicker
//    fun pickImage() {
//        val intent = Intent(Intent.ACTION_PICK)
//        intent.type = "image/*"
//        startActivityForResult(intent, 102)}
//
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == RESULT_OK && requestCode == 102) {
//            val uri = data?.data
//            // Assuming 'profilePic' is the ImageView where you want to display the selected image
//            profilePic.setImageURI(uri)
//            // If you want to upload the selected image to the server, you can call a function here
//            // and pass the 'uri' to that function.
//
//    }//end

        titleList = arrayOf(
            "Name",
            "EMail",
            "Phone Number",
            "Date Joined",

        )
        infoList = arrayOf(
            "Ajith",
            "Ajith@gmail.com",
            "+94 77 123 4567",
            "1/1/2024"
        )

        recyclerView = findViewById(R.id.viewProfileRW)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        dataList = arrayListOf<ViewProfileDataClass>()
        getData()
    }
    private fun getData() {
        for (i in titleList.indices) {
            val dataClass = ViewProfileDataClass(titleList[i], infoList[i])
            dataList.add(dataClass)
        }
        recyclerView.adapter = ViewProfileAdapterClass(dataList)
    }
}

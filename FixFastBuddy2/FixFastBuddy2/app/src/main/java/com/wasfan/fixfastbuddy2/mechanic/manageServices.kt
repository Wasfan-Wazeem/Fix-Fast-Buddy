package com.wasfan.fixfastbuddy2.mechanic

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wasfan.fixfastbuddy2.R
import com.wasfan.fixfastbuddy2.databinding.ActivityManageServicesBinding
import com.wasfan.fixfastbuddy2.mechanic.AdapterClass.AdapterClassMS
import com.wasfan.fixfastbuddy2.mechanic.DataClass.DataClassMS
//class manageServices : AppCompatActivity() {
//
//    private lateinit var backbtn3:ImageButton
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var dataList: ArrayList<DataClassMS>
//    lateinit var titleList:Array<String>
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_manage_services)
//        // Find the LinearLayout and textview by its ID
//        val EditButton: TextView = view.findViewById(R.id.editMS)
//
//        backbtn3 = findViewById(R.id.backBtn3)
//        backbtn3.setOnClickListener {
//            onBackPressed()}
//
//        titleList = arrayOf(
//            "Tire",
//            "Fuel",
//            "ImageView"
//        )
//
//
//        recyclerView = findViewById(R.id.mngservices_rv)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.setHasFixedSize(true)
//
//        dataList = arrayListOf<DataClassMS>()
//        getData()
//
//    }
//    private fun getData(){
//        for (i in titleList.indices){
//            val dataClass = DataClassMS( titleList[i])
//            dataList.add(dataClass)
//        }
//        recyclerView.adapter = AdapterClassMS(dataList)
//
//    }
//}
//
//
//
//



class manageServices : AppCompatActivity() {

    private lateinit var binding: ActivityManageServicesBinding
    private lateinit var backbtn3: ImageButton
    private lateinit var editMS:ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<DataClassMS>
    private lateinit var titleList: Array<String>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_services)


        backbtn3 = findViewById(R.id.backBtn3)
        backbtn3.setOnClickListener {
            onBackPressed()}



// Inside your activity's onCreate method or wherever appropriate
                editMS = findViewById(R.id.editMS)
        editMS.setOnClickListener {
            // Create an Intent to navigate to the Edit Services activity
            val intent = Intent(this@manageServices, EditServices::class.java)
            startActivity(intent)
        }



        // Initialize other views and data
        titleList = arrayOf(
            "Tire",
            "Fuel",
            "ImageView"
        )
        recyclerView = binding.mngservicesRv
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        dataList = arrayListOf<DataClassMS>()
        getData()
    }

    private fun getData() {
        for (i in titleList.indices) {
            val dataClass = DataClassMS(titleList[i])
            dataList.add(dataClass)
        }
        recyclerView.adapter = AdapterClassMS(dataList)
    }
}


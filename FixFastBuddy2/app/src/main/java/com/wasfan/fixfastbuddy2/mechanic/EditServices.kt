package com.wasfan.fixfastbuddy2.mechanic

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wasfan.fixfastbuddy2.R
import com.wasfan.fixfastbuddy2.databinding.ActivityEditServicesBinding
import com.wasfan.fixfastbuddy2.mechanic.AdapterClass.EditServicesAdapterClass
import com.wasfan.fixfastbuddy2.mechanic.DataClass.EditServicesDataClass

private lateinit var binding: ActivityEditServicesBinding
private lateinit var backbtnES: ImageButton
private lateinit var recyclerView: RecyclerView
private lateinit var dataList: ArrayList<EditServicesDataClass>
private lateinit var Checkbox:Array<Boolean>
private lateinit var titles: Array<String>

class EditServices : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_edit_services)
        binding = ActivityEditServicesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        backbtnES = findViewById(R.id.backBtnES)
        backbtnES.setOnClickListener {
            onBackPressed()}

        // Initialize other views and data
//        Checkbox = arrayOf("T",
//            "F")

        titles = arrayOf(
            "Tire",
            "Fuel",
        )

        recyclerView = binding.editservicesRv
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        dataList = arrayListOf<EditServicesDataClass>()
        Checkbox = Array(titles.size){false}
        getData()
    }

    private fun getData() {
        for (i in titles.indices) {
            val dataClass = EditServicesDataClass(titles[i], Checkbox[i])
            dataList.add(dataClass)
        }
        recyclerView.adapter = EditServicesAdapterClass(dataList)
    }
}

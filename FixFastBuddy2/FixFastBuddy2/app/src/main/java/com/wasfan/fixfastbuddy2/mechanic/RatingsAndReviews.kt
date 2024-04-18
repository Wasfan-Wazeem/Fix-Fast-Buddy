package com.wasfan.fixfastbuddy2.mechanic

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wasfan.fixfastbuddy2.R
import com.wasfan.fixfastbuddy2.mechanic.AdapterClass.RatingsAdapterClass
import com.wasfan.fixfastbuddy2.mechanic.DataClass.RatingsDataClass

class RatingsAndReviews : AppCompatActivity() {

    private lateinit var backBtn4: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<RatingsDataClass>
    lateinit var  ratingBar: Array<Float> //correct?
    lateinit var Name:Array<String>
    lateinit var Date:Array<String>
    lateinit var Review:Array<String>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ratings_and_reviews)

        backBtn4 = findViewById(R.id.backBtn4)
        backBtn4.setOnClickListener {
            onBackPressed()}

        ratingBar = arrayOf(
            2.5f,
            3.0f,
            5.0f
        )

        Name = arrayOf(
            "Alexander",
            "Smith",
            "Peter"
        )
        Date = arrayOf(
            "1/1/2024",
            "1/1/2024",
            "1/1/2024"
        )
        Review = arrayOf(
            "Excellent",
            "Good",
            "Average"
        )

        recyclerView = findViewById(R.id.ratings_rv)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        dataList = arrayListOf<RatingsDataClass>()
        getData()
    }
    private fun getData(){
        for (i in ratingBar.indices){
            val dataClass = RatingsDataClass(ratingBar[i], Name[i],
                Date[i], Review[i])
            dataList.add(dataClass)
        }
        recyclerView.adapter = RatingsAdapterClass(dataList)
    }
}
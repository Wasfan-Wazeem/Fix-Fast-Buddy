package com.wasfan.fixfastbuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.wasfan.fixfastbuddy.dataClasses.requestDataClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RatingAndReview : AppCompatActivity() {

    private lateinit var ratingBar: RatingBar
    private lateinit var submitBTN: Button
    private lateinit var mechanicNameTV: TextView
    private lateinit var garageNameTV: TextView
    private lateinit var reviewET: EditText
    private lateinit var skipTV: TextView

    private lateinit var requestId: String
    private lateinit var requestData: requestDataClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating_and_review)

        init()

        lifecycleScope.launch {
            fetchRequestDetails(requestId)
        }

        setupRatingBar()

        submitBTN.setOnClickListener {
            if (reviewET.text.isEmpty()) {
                Toast.makeText(this@RatingAndReview, "Please enter a review", Toast.LENGTH_LONG)
                    .show()
            } else if (requestData.rating == null) {
                Toast.makeText(this@RatingAndReview, "Please give a rating", Toast.LENGTH_LONG)
                    .show()
            } else {
                requestData.review = reviewET.text.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    updateRatingAndReview(requestId, requestData.rating!!, requestData.review!!)
                }
            }
        }

        skipTV.setOnClickListener {
            startActivity(Intent(this@RatingAndReview, Navigation::class.java))
            finish()
        }
    }

    private suspend fun updateRatingAndReview(requestId: String, rating: Float, review: String) {
        withContext(Dispatchers.IO) {
            val retrofitAPI = RetrofitInstance.api
            val call: Call<ResponseBody> =
                retrofitAPI.updateRatingAndReview(requestId, rating, review)

            try {
                val response: Response<ResponseBody> = call.execute()
                if (response.isSuccessful) {
                    val responseBody: ResponseBody? = response.body()
                    responseBody?.let {
                        println(responseBody)
                        startActivity(Intent(this@RatingAndReview, Navigation::class.java))
                        finish()
                    }
                } else {
                    println("Failed to update Rating and Review: ${response.message()}")
                }
            } catch (e: Exception) {
                println("Failed to update Rating and Review: ${e.message}")
            }
        }
    }

    private suspend fun fetchRequestDetails(requestId: String) {
        val apiService = RetrofitInstance.api

        val call = apiService.fetchRequestDetailsRequestId(requestId)
        call.enqueue(object : Callback<requestDataClass> {
            override fun onResponse(
                call: Call<requestDataClass>,
                response: Response<requestDataClass>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        requestData = data
                        println(requestData)
                        updateUI()
                    } else {
                        println("Failed to fetch user details")
                        Toast.makeText(
                            this@RatingAndReview,
                            "Failed to fetch user details",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    println("Failed to fetch user details")
                    Toast.makeText(
                        this@RatingAndReview,
                        "Failed to fetch user details",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<requestDataClass>, t: Throwable) {
                println("Network error: ${t.message}")
                Toast.makeText(
                    this@RatingAndReview,
                    "Network error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun setupRatingBar() {
        ratingBar.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { _, rating, _ ->
            requestData.rating = rating

        }
    }

    private fun init() {
        requestId = intent.getStringExtra("requestId")!!
        ratingBar = findViewById(R.id.ratingBar)
        submitBTN = findViewById(R.id.submitBTN)
        mechanicNameTV = findViewById(R.id.mechanicNameTV)
        garageNameTV = findViewById(R.id.garageNameTV)
        reviewET = findViewById(R.id.reviewET)
        skipTV = findViewById(R.id.skipTV)
    }

    private fun updateUI() {
        mechanicNameTV.text = "${requestData.firstName} ${requestData.lastName}"
        garageNameTV.text = requestData.garageId

    }

}
package com.wasfan.fixfastbuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast

class RatingAndReview : AppCompatActivity() {

    private lateinit var ratingBar: RatingBar
    private lateinit var submitBTN: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating_and_review)

        ratingBar = findViewById(R.id.ratingBar)
        submitBTN = findViewById(R.id.submitBTN)

        submitBTN.setOnClickListener {
            startActivity(Intent(this@RatingAndReview, Navigation::class.java))
        }

        setupRatingBar()
    }

    private fun setupRatingBar() {
        ratingBar.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { _, rating, _ ->
            Toast.makeText(this@RatingAndReview, "Rating: $rating", Toast.LENGTH_SHORT).show()
        }
    }

}
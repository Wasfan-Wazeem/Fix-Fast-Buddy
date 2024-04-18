package com.wasfan.fixfastbuddy2.mechanic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wasfan.fixfastbuddy2.R

class Navigation : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)



        bottomNavigationView = findViewById(R.id.bottom_navigation)

 //Get information passed through Intent extras
        val fragmentToShow = intent.getStringExtra("fragment_to_show")

        // Check which fragment to show
        if (fragmentToShow == "home") {
            replacefragment(HomeFragment())
        } else {
            // Default to HomeFragment
            replacefragment(HomeFragment())
        }




        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_home -> {
                    replacefragment(HomeFragment())
                    true
                }

                R.id.bottom_activities -> {
                    replacefragment(ActivitiesFragment())
                    true
                }

                R.id.bottom_profile -> {
                    replacefragment(Account())
                    true
                }

                else -> false
            }
        }
        replacefragment(HomeFragment())

    }

    private fun replacefragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frameContainer, fragment).commit()
    }


}
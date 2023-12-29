package com.wasfan.fixfastbuddy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Navigation : AppCompatActivity() {

    private lateinit var bottomNavigationView : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.bottom_home -> {
                    replacefragment(HomeFragment())
                    true
                }
                R.id.bottom_activites -> {
                    replacefragment(ActivitiesFragment())
                    true
                }
                R.id.bottom_notifications -> {
                    replacefragment(NotificationsFragment())
                    true
                }
                R.id.bottom_profile -> {
                    replacefragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
        replacefragment(HomeFragment())

    }

    private fun replacefragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frameContainer, fragment).commit()
    }

}
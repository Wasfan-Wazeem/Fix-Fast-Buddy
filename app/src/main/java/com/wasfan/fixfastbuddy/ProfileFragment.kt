package com.wasfan.fixfastbuddy

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    private lateinit var auth : FirebaseAuth
    private lateinit var logOut : LinearLayout
    private lateinit var viewProfile : LinearLayout
    private lateinit var fullNameTV : TextView

    private val currentUser = FirebaseAuth.getInstance().currentUser
    val phoneNumber = currentUser?.phoneNumber.toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        auth = FirebaseAuth.getInstance()
        logOut = view.findViewById(R.id.logOut)
        viewProfile = view.findViewById(R.id.viewProfile)
        fullNameTV = view.findViewById(R.id.profileName)

        getUserInfo(phoneNumber)

        viewProfile.setOnClickListener {
            startActivity(Intent(requireContext(), ViewProfile::class.java))
        }

        logOut.setOnClickListener {
            auth.signOut()
            val intent = Intent(Intent(requireContext(), Phone::class.java))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            requireActivity().onBackPressed()
        }




        return view
    }

    private fun getUserInfo(phoneNumber: String){
        val apiService = RetrofitInstance.api

        val call = apiService.getUserDetails(phoneNumber)
        call.enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        fullNameTV.text = "${user.firstName} ${user.lastName}"
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch user details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                Toast.makeText(requireContext(), "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
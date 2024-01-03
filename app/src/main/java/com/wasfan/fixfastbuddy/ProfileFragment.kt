package com.wasfan.fixfastbuddy

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var auth : FirebaseAuth
    private lateinit var logOut : LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        auth = FirebaseAuth.getInstance()
        logOut = view.findViewById(R.id.logOut)

        logOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(requireContext(), Phone::class.java))
        }

        return view
    }
}
package com.wasfan.fixfastbuddy2.mechanic

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.wasfan.fixfastbuddy2.R

//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"
//
///**
// * A simple [Fragment] subclass.
// * Use the [Account.newInstance] factory method to
// * create an instance of this fragment.
// */
//class Account : Fragment() {
    // TODO: Rename and change types of parameters
    private var param3: String? = null
    private var param4: String? = null

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?,
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_account, container, false)
//    }
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment Account.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            Account().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
//}

class Account : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the LinearLayout and textview by its ID
        val viewProfile: TextView = view.findViewById(R.id.viewProfileText)
        val earningsLayout: LinearLayout = view.findViewById(R.id.earnings)
        val ratingsAndReviewsLayout: LinearLayout = view.findViewById(R.id.ratingsAndReviews)
        val manageServicesLayout: LinearLayout = view.findViewById(R.id.manageServices)
//        val helpAndSupportLayout: LinearLayout = view.findViewById(R.id.helpAndSupport)
//        val aboutUsLayout: LinearLayout = view.findViewById(R.id.aboutUs)

        // Set OnClickListener to navigate to the ViewProfile activity
        viewProfile.setOnClickListener {
            // Create an Intent to navigate to the ViewProfile activity
            val intent = Intent(activity, ViewProfile::class.java)
            startActivity(intent)
        }

        // Set OnClickListener to navigate to the earnings activity
        earningsLayout.setOnClickListener {
            // Create an Intent to navigate to the earnings activity
            val intent = Intent(activity, Earnings::class.java)
            startActivity(intent)
        }

        ratingsAndReviewsLayout.setOnClickListener {
            // Create an Intent to navigate to the RatingsAndReview activity
            val intent = Intent(activity, RatingsAndReviews::class.java)
            startActivity(intent)
        }

        manageServicesLayout.setOnClickListener {
            // Create an Intent to navigate to the manageServices activity
            val intent = Intent(activity, manageServices::class.java)
            startActivity(intent)
        }

//        helpAndSupportLayout.setOnClickListener {
//            // Create an Intent to navigate to the earnings activity
//            val intent = Intent(activity, HelpAndSupport::class.java)
//            startActivity(intent)
//        }

//        aboutUsLayout.setOnClickListener {
//            // Create an Intent to navigate to the earnings activity
//            val intent = Intent(activity, Earnings::class.java)
//            startActivity(intent)
//        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Account.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Account().apply {
                arguments = Bundle().apply {
                    val ARG_PARAM3 = null
                    putString(ARG_PARAM3, param3)
                    val ARG_PARAM4 = null.also {
                        putString(it, param4)
                    }
                }
            }
    }
}

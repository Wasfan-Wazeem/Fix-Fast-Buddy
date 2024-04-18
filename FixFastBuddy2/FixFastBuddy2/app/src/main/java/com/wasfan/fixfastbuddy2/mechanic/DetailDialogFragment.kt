package com.wasfan.fixfastbuddy2.mechanic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.wasfan.fixfastbuddy2.R

class DetailDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detail_dialog, container, false)

        // Retrieve arguments passed to the fragment
        val name = arguments?.getString("name")
        val date = arguments?.getString("date")
        val task = arguments?.getString("task")

        // Display the data in the TextViews
        view.findViewById<TextView>(R.id.tv_name).text = name
        view.findViewById<TextView>(R.id.tv_date).text = date
        view.findViewById<TextView>(R.id.tv_task).text = task

        // Handle back button click
        view.findViewById<Button>(R.id.btn_back).setOnClickListener {
            dismiss() // Dismiss the dialog when the back button is clicked
        }

        return view
    }
}

package com.wasfan.fixfastbuddy

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.wasfan.fixfastbuddy.adapterClasses.CancellationReasonAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {


    private lateinit var PopupBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PopupBtn = findViewById(R.id.popupBtn)

        PopupBtn.setOnClickListener {

            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.cancelling_service_popup_dialog)
            dialog.window!!.setBackgroundDrawable(getDrawable(R.drawable.cancelling_dialog_bg))

            val layoutParams = dialog.window?.attributes
            layoutParams?.gravity = Gravity.BOTTOM
            layoutParams?.width =
                ViewGroup.LayoutParams.MATCH_PARENT // Set the width to wrap content
            layoutParams?.height =
                ViewGroup.LayoutParams.WRAP_CONTENT // Set the height to wrap content
            dialog.window!!.attributes = layoutParams

            val reasonsList = listOf("Reason 1", "Reason 2", "Reason 3")

            val adapter = CancellationReasonAdapter(reasonsList) { position ->
                // Handle item click, position is the clicked item's position in the list
                Toast.makeText(this@MainActivity, "Your reason - ${reasonsList[position]}", Toast.LENGTH_LONG).show()
            }

            val recyclerView =
                dialog.findViewById<RecyclerView>(R.id.cancellationReasonsRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter

            dialog.findViewById<Button>(R.id.confirmButton).setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }
}
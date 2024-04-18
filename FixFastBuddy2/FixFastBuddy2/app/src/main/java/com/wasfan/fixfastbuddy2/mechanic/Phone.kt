package com.wasfan.fixfastbuddy2.mechanic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.wasfan.fixfastbuddy2.R
import java.util.concurrent.TimeUnit

class Phone : AppCompatActivity() {

    private lateinit var sendOTPBtn: Button
    private lateinit var phoneNumberET: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var number: String
    private lateinit var mProgressBar: ProgressBar
    private lateinit var countryCodeSpinner : Spinner
    private lateinit var radioGroup : RadioGroup
    private lateinit var selectedRadioButton : RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone)

        init()
        cCodeSpinner()
        sendOTPBtn.setOnClickListener {
            number = phoneNumberET.text.trim().toString()
            if (number.isNotEmpty()) {
                if (number.length == 9) {
                    val cCode = countryCodeSpinner.selectedItem.toString()
                    number = "$cCode$number"
                    mProgressBar.visibility = View.VISIBLE
                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(number) // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this) // Activity (for callback binding)
                        .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)

                } else {
                    Toast.makeText(this, "Please Enter Correct Number", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Please Enter Number", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun init() {
        mProgressBar = findViewById(R.id.phoneProgressBar)
        mProgressBar.visibility = View.INVISIBLE
        sendOTPBtn = findViewById(R.id.sendOTPbutton)
        phoneNumberET = findViewById(R.id.loginNumber)
        auth = FirebaseAuth.getInstance()
        countryCodeSpinner = findViewById(R.id.countryCodeSpinner)
        radioGroup = findViewById(R.id.radioGroup)

        val defaultRadioButton = findViewById<RadioButton>(R.id.radioButtonIndividual)
        defaultRadioButton.isChecked = true
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this, "Authenticate Successfully", Toast.LENGTH_SHORT).show()
                    sendToMain()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.d("TAG", "signInWithPhoneAuthCredential: ${task.exception.toString()}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }

    private fun sendToMain() {
        startActivity((Intent(this, Navigation::class.java)))
        finish()
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Log.d("TAG", "onVerificationFailed: ${e.toString()}")
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Log.d("TAG", "onVerificationFailed: ${e.toString()}")
            } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                // reCAPTCHA verification attempted with null Activity
                Log.d("TAG", "onVerificationFailed: ${e.toString()}")
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            // Save verification ID and resending token so we can use them later
            val selectedRadioButtonId: Int = radioGroup.checkedRadioButtonId
            selectedRadioButton = findViewById(selectedRadioButtonId)
            val radioString: String = selectedRadioButton.text.toString()

            val intent = Intent(this@Phone, OTP::class.java)
            intent.putExtra("OTP", verificationId)
            intent.putExtra("resendToken", token)
            intent.putExtra("phoneNumber", number)
            intent.putExtra("mechanicType", radioString)
            startActivity(intent)
            mProgressBar.visibility = View.INVISIBLE
            finish()
        }
    }

    private fun cCodeSpinner() {
        val listItemsType = listOf("+94", "+91", "+11", "+47", "+74", "+86")

        val arrayAdapterType =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, listItemsType)
        arrayAdapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        countryCodeSpinner.adapter = arrayAdapterType
        countryCodeSpinner.setSelection(0)

        countryCodeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    // Handle the hint selection (optional)
                } else {
                    val selectedItem = parent?.getItemAtPosition(position)?.toString()
                    Toast.makeText(
                        this@Phone,
                        "Your car Make is $selectedItem",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }


    }

}
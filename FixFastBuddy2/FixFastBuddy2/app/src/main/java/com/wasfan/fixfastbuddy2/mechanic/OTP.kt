package com.wasfan.fixfastbuddy2.mechanic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class OTP : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var verifyBtn: Button
    private lateinit var resendTV: TextView
    private lateinit var inputOTP1: EditText
    private lateinit var inputOTP2: EditText
    private lateinit var inputOTP3: EditText
    private lateinit var inputOTP4: EditText
    private lateinit var inputOTP5: EditText
    private lateinit var inputOTP6: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var otpNumberTV : TextView
    private lateinit var editTextTV : TextView

    private lateinit var OTP: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var phoneNumber: String
    private lateinit var mechanicType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        OTP = intent.getStringExtra("OTP").toString()
        resendToken = intent.getParcelableExtra("resendToken")!!
        phoneNumber = intent.getStringExtra("phoneNumber")!!
        mechanicType = intent.getStringExtra("mechanicType")!!

        init()
        otpNumberTV.text = phoneNumber
        progressBar.visibility = View.INVISIBLE
        addTextChangeListener()
        resendOTPvVisibility()


        resendTV.setOnClickListener {
            resendVerificationCode()
            resendOTPvVisibility()
        }

        verifyBtn.setOnClickListener {
            //collect otp from all the edit texts
            val typedOTP =
                (inputOTP1.text.toString() + inputOTP2.text.toString() + inputOTP3.text.toString() + inputOTP4.text.toString() + inputOTP5.text.toString() + inputOTP6.text.toString())

            if (typedOTP.isNotEmpty()) {
                if (typedOTP.length == 6) {
                    val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                        OTP, typedOTP
                    )
                    progressBar.visibility = View.VISIBLE
                    signInWithPhoneAuthCredential(credential)
                } else {
                    Toast.makeText(this, "Please Enter Correct OTP", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please Enter OTP", Toast.LENGTH_SHORT).show()
            }
        }

        editTextTV.setOnClickListener{
            startActivity(Intent(this@OTP, Phone::class.java))
            finish()
        }

    }

    private fun resendOTPvVisibility() {
        inputOTP1.setText("")
        inputOTP2.setText("")
        inputOTP3.setText("")
        inputOTP4.setText("")
        inputOTP5.setText("")
        inputOTP6.setText("")
        resendTV.visibility = View.INVISIBLE
        resendTV.isEnabled = false

        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            resendTV.visibility = View.VISIBLE
            resendTV.isEnabled = true
        }, 30000)
    }

    private fun resendVerificationCode() {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks)
            .setForceResendingToken(resendToken)// OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
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
            OTP = verificationId
            resendToken = token
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    progressBar.visibility = View.VISIBLE
                    Toast.makeText(this, "Authenticate Successfully", Toast.LENGTH_SHORT).show()

                    if(mechanicType == "Individual"){
                        checkMechanicInDatabase(phoneNumber)
                    }else if(mechanicType == "Garage"){
                        checkGarageMechanicInDatabase(phoneNumber)
                    }
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

    private fun sendToSignUp() {
        val intent = (Intent(this, Navigation::class.java))
        intent.putExtra("phoneNumber", phoneNumber)
        intent.putExtra("mechanicType", mechanicType)
        startActivity(intent)
        finish()
    }

    private fun checkMechanicInDatabase(phoneNumber: String) {

        val apiService = RetrofitInstance.api

        val call = apiService.checkMechanic(phoneNumber)

        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    val isAvailable = response.body() ?: false
                    if (isAvailable) {
                        // Phone number exists in the database
                        Toast.makeText(this@OTP, "Mechanic phone number exists", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@OTP, Navigation::class.java))
                        finish()
                    } else {
                        // Phone number does not exist in the database
                        Toast.makeText(this@OTP, "Mechanic Phone number does not exist", Toast.LENGTH_SHORT).show()
                        sendToSignUp()
                    }
                } else {
                    // Handle error response
                    Toast.makeText(this@OTP, "Error checking phone number", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                // Handle network or unexpected errors
                Toast.makeText(this@OTP, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkGarageMechanicInDatabase(phoneNumber: String) {

        val apiService = RetrofitInstance.api

        val call = apiService.checkGarageMechanic(phoneNumber)

        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    val isAvailable = response.body() ?: false
                    if (isAvailable) {
                        // Phone number exists in the database
                        Toast.makeText(this@OTP, "Garage Mechanic phone number exists", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@OTP, Navigation::class.java))
                        finish()
                    } else {
                        // Phone number does not exist in the database
                        Toast.makeText(this@OTP, "Garage Mechanic Phone number does not exist", Toast.LENGTH_SHORT).show()
                        sendToSignUp()
                    }
                } else {
                    // Handle error response
                    Toast.makeText(this@OTP, "Error checking phone number", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                // Handle network or unexpected errors
                Toast.makeText(this@OTP, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun addTextChangeListener() {
        inputOTP1.addTextChangedListener(EditTextWatcher(inputOTP1))
        inputOTP2.addTextChangedListener(EditTextWatcher(inputOTP2))
        inputOTP3.addTextChangedListener(EditTextWatcher(inputOTP3))
        inputOTP4.addTextChangedListener(EditTextWatcher(inputOTP4))
        inputOTP5.addTextChangedListener(EditTextWatcher(inputOTP5))
        inputOTP6.addTextChangedListener(EditTextWatcher(inputOTP6))
    }

    private fun init() {
        auth = FirebaseAuth.getInstance()
        progressBar = findViewById(R.id.otpProgressBar)
        verifyBtn = findViewById(R.id.verifyButton)
        resendTV = findViewById(R.id.resend_code)
        inputOTP1 = findViewById(R.id.otpNumber1)
        inputOTP2 = findViewById(R.id.otpNumber2)
        inputOTP3 = findViewById(R.id.otpNumber3)
        inputOTP4 = findViewById(R.id.otpNumber4)
        inputOTP5 = findViewById(R.id.otpNumber5)
        inputOTP6 = findViewById(R.id.otpNumber6)
        otpNumberTV = findViewById((R.id.otpNumber))
        editTextTV = findViewById(R.id.editText)
    }


    inner class EditTextWatcher(private val view: View) : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(p0: Editable?) {
            val text = p0.toString()
            when (view.id) {
                R.id.otpNumber1 -> if (text.length == 1) inputOTP2.requestFocus()
                R.id.otpNumber2 -> if (text.length == 1) inputOTP3.requestFocus() else if (text.isEmpty()) inputOTP1.requestFocus()
                R.id.otpNumber3 -> if (text.length == 1) inputOTP4.requestFocus() else if (text.isEmpty()) inputOTP2.requestFocus()
                R.id.otpNumber4 -> if (text.length == 1) inputOTP5.requestFocus() else if (text.isEmpty()) inputOTP3.requestFocus()
                R.id.otpNumber5 -> if (text.length == 1) inputOTP6.requestFocus() else if (text.isEmpty()) inputOTP4.requestFocus()
                R.id.otpNumber6 -> if (text.isEmpty()) inputOTP5.requestFocus()
            }
        }
    }
}
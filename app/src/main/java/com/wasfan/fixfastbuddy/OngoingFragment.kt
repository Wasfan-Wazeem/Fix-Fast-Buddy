package com.wasfan.fixfastbuddy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.wasfan.fixfastbuddy.adapterClasses.OngoingAdapterClass
import com.wasfan.fixfastbuddy.dataClasses.FetchCancelledDataClass
import com.wasfan.fixfastbuddy.dataClasses.OngoingDataClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

class OngoingFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<OngoingDataClass>
    private lateinit var imageList: Array<Int>
    private lateinit var serviceID: Array<String>
    private lateinit var serviceType: Array<String>
    private lateinit var vehicleNum: Array<String>
    private lateinit var address: Array<String>
    private lateinit var date: Array<String>
    private lateinit var time: Array<String>
    private lateinit var status: Array<String?>
    private lateinit var mechanicPhoneNumber: Array<String?>
    private lateinit var locationDistance: Array<String>

    private val currentUser = FirebaseAuth.getInstance().currentUser
    val phoneNumber = currentUser?.phoneNumber.toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ongoing, container, false)

        recyclerView = view.findViewById(R.id.ongoingRecyclerView)

        lifecycleScope.launch(Dispatchers.IO){
            fetchOngoingRequest(phoneNumber)
        }

        return view
    }

    private suspend fun fetchOngoingRequest(phoneNumber: String) {
        withContext(Dispatchers.IO) {
            val retrofitAPI = RetrofitInstance.api
            val call: Call<List<FetchCancelledDataClass>> =
                retrofitAPI.fetchOngoingRequest(phoneNumber)

            try {
                val response: Response<List<FetchCancelledDataClass>> = call.execute()
                if (response.isSuccessful) {
                    val responseBody: List<FetchCancelledDataClass>? = response.body()
                    responseBody?.let {
                        val data: List<FetchCancelledDataClass> = responseBody
                        withContext(Dispatchers.Main) {
                            imageList = Array(data.size){Services.servicesList[0].servicesImage}
                            serviceID = data.map { it.requestId }.toTypedArray()
                            serviceType = data.map { it.serviceType }.toTypedArray()
                            vehicleNum = data.map { it.licensePlateNo }.toTypedArray()
                            address = data.map { it.userInitialAddress }.toTypedArray()
                            date = data.map { it.date }.toTypedArray()
                            time = data.map { it.time }.toTypedArray()
                            status = data.map { it.status }.toTypedArray()
                            mechanicPhoneNumber = data.map { it.mechanicPhoneNumber }.toTypedArray()
                            locationDistance = Array(data.size){""}

                            withContext(Dispatchers.Main) {
                                recyclerView()
                            }
                        }
                    }
                } else {
                    println("Failed to update location: ${response.message()}")
                }
            } catch (e: Exception) {
                println("Failed to update location: ${e.message}")
            }
        }
    }

    private fun recyclerView(){
        val onTrackClickListener = object : (Int) -> Unit {
            override fun invoke(position: Int) {
                // Handle track button click for item at position
                Manager.onGoingRequestId = serviceID[position]
                startActivity(Intent(requireContext(), OngoingService::class.java))
            }
        }

        val onCallClickListener = object : (Int) -> Unit {
            override fun invoke(position: Int) {
                val intent =
                    Intent(Intent.ACTION_DIAL, Uri.parse("tel:${mechanicPhoneNumber[position]}"))
                startActivity(intent)
            }
        }

        val onMessageClickListener = object : (Int) -> Unit {
            override fun invoke(position: Int) {
                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${mechanicPhoneNumber[position]}"))
                startActivity(intent)
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        dataList = arrayListOf<OngoingDataClass>()
        for(i in imageList.indices){
            val dataClass = OngoingDataClass(serviceID[i], serviceType[i], vehicleNum[i], address[i], date[i], time[i], status[i], locationDistance[i])
            dataList.add(dataClass)
        }
        recyclerView.adapter = OngoingAdapterClass(dataList, onTrackClickListener, onCallClickListener, onMessageClickListener)
    }

}
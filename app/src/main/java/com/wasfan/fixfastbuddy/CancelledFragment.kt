package com.wasfan.fixfastbuddy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.wasfan.fixfastbuddy.adapterClasses.CancelledAdapterClass
import com.wasfan.fixfastbuddy.dataClasses.FetchCancelledDataClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

class CancelledFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<CompletedDataClass>
    private lateinit var imageList: Array<Int?>
    private lateinit var serviceID: Array<String>
    private lateinit var serviceType: Array<String>
    private lateinit var vehicleNum: Array<String>
    private lateinit var address: Array<String>
    private lateinit var date: Array<String>
    private lateinit var time: Array<String>
    private lateinit var completedAmount: Array<String?>

    private val currentUser = FirebaseAuth.getInstance().currentUser
    val phoneNumber = currentUser?.phoneNumber.toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cancelled, container, false)

        recyclerView = view.findViewById(R.id.cancelledRecyclerView)

        lifecycleScope.launch(Dispatchers.IO) {
            fetchCancelledRequests(phoneNumber)
        }

        return view
    }

    private suspend fun fetchCancelledRequests(phoneNumber: String) {
        withContext(Dispatchers.IO) {
            val retrofitAPI = RetrofitInstance.api
            val call: Call<List<FetchCancelledDataClass>> =
                retrofitAPI.fetchCancelledRequests(phoneNumber)

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
                            completedAmount = data.map { it.totalAmount }.toTypedArray()

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

    private fun recyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        dataList = arrayListOf<CompletedDataClass>()
        for (i in imageList.indices) {
            val dataClass = CompletedDataClass(
                imageList[i],
                serviceID[i],
                serviceType[i],
                vehicleNum[i],
                address[i],
                date[i],
                time[i],
                completedAmount[i]
            )
            dataList.add(dataClass)
        }
        recyclerView.adapter = CancelledAdapterClass(dataList)
    }

}
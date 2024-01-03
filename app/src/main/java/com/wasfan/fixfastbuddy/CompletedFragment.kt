package com.wasfan.fixfastbuddy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CompletedFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<CompletedDataClass>
    private lateinit var imageList: Array<Int>
    private lateinit var serviceID: Array<String>
    private lateinit var serviceType: Array<String>
    private lateinit var vehicleNum: Array<String>
    private lateinit var address: Array<String>
    private lateinit var date: Array<String>
    private lateinit var time: Array<String>
    private lateinit var completedAmount: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_completed, container, false)

        initLists()

        recyclerView = view.findViewById(R.id.completedRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        dataList = arrayListOf<CompletedDataClass>()
        getData()

        return view
    }

    private fun getData(){
        for(i in imageList.indices){
            val dataClass = CompletedDataClass(imageList[i], serviceID[i], serviceType[i], vehicleNum[i], address[i], date[i], time[i], completedAmount[i])
            dataList.add(dataClass)
        }
        recyclerView.adapter = CompletedAdapterClass(dataList)
    }

    private fun initLists(){
        imageList = arrayOf(
            R.drawable.service_icon,
            R.drawable.hospital
        )

        serviceID = arrayOf(
            "S15518209",
            "S16251325"
        )

        serviceType = arrayOf(
            "Tyre",
            "Engine"
        )

        vehicleNum = arrayOf(
            "CAC-0282",
            "CBH-0484"
        )

        address = arrayOf(
            "123-131 Philip Gunawardena Mawatha, Colombo 00400",
            "252 2/1 Matale Road Katugastota"
        )

        date = arrayOf(
            "2023-10-21",
            "2024-01-01"
        )

        time = arrayOf(
            "17:23:29",
            "20:16:47"
        )

        completedAmount = arrayOf(
            "LKR 2,150.00",
            "LKR 950.00"
        )
    }

}